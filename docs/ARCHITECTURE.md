# BookMyShow — Architecture

Layered Spring Boot service (Spring Boot 3.4.5, Java 24, MySQL via Spring Data JPA).

## Layers

- **`Controllers/`** — thin REST adapters. Should contain no business logic and must not touch
  `Repositories/` directly.
- **`Services/`** — transactional business logic (booking, payment, sign-up, pricing).
- **`Repositories/`** — Spring Data JPA interfaces, one per aggregate root.
- **`Models/`** — JPA entities, all extending `BaseModel` (`id`, `createdAt`, `modifiedAt`).
  Enums live in `Models/enums/`.
- **`dtos/`** — request/response payloads; entities must never be serialized directly over the
  wire.
- **`Exceptions/`** — custom `RuntimeException` subclasses used by the service layer.
- **`configs/`** — `@Configuration` beans (currently just a `BCryptPasswordEncoder`).

## Package layout

```
src/main/java/dev/anil/bookmyshow/
├── BookMyShowApplication.java
├── Controllers/   BookingController, UserController
├── Exceptions/    6 custom exceptions
├── Models/        Actor, Booking, City, Movie, Payment, Screen, Seat, Show, Show_Seat,
│                  Theater, User, BaseModel  (+ Models/enums/)
├── Repositories/  BookingRepository, ShowRepository, Show_SeatRepository, UserRepository
├── Services/      BookingService, PaymentService, PriceCalculationService, SignUpService
├── configs/       bCryptPasswordEncoder
└── dtos/          request/response payloads
```

## Request flows

**Booking** — `POST /book`:

```
BookingController.book()
  -> BookingService.book(userId, showId, showSeatIds)
       -> UserRepository.findUserById()
       -> ShowRepository.findById()
       -> Show_SeatRepository.findAllByIdAndStatus(ids, AVAILABLE)   -- @Lock(PESSIMISTIC_WRITE)
       -> Show_SeatRepository.updateShowSeats(seats, BLOCKED)        -- @Modifying bulk update
       -> PriceCalculationService.calculate(seats)
       -> new Booking(status = PENDING)
       -> PaymentService.pay(amount)
       -> booking.status = CONFIRMED
```

Runs inside a single `@Transactional(isolation = SERIALIZABLE, rollbackFor = Exception.class)`
boundary spanning the seat lock, price calculation, and payment call.

**Sign-up** — `POST /signup`:

```
UserController.signup()
  -> SignUpService.signup(username, password, email, phone)
       -> UserRepository.existsByEmail() / existsUserByMobile()   -- throws UserAlreadyExistException
       -> BCryptPasswordEncoder.encode(password)
       -> UserRepository.save(user)
```

## Known gaps

These are current, real gaps in the codebase — useful context when reviewing diffs that touch
these areas, since a diff that "fixes" one of them is expected, and a diff that assumes the gap
is already fixed is not:

- **No CRUD stack for 7 entities.** `Movie`, `Theater`, `Screen`, `Seat`, `City`, `Actor`, and
  `Payment` exist only as JPA models — no `Repository`, `Service`, or `Controller` yet.
- **`BookingController` is `@Controller`, not `@RestController`.** It returns a DTO body without
  `@ResponseBody`, so responses are not guaranteed to be serialized as JSON.
- **`BookingService.book()` never calls `bookingRepository.save(booking)`.** The `Booking` is
  built and mutated in memory but the visible code path does not appear to persist it.
- **No global exception handling.** Despite 6 custom exception classes in `Exceptions/`,
  there is no `@ControllerAdvice`; both controllers catch exceptions internally and return a
  `ResponseStatus.FAILURE` DTO with a 200 status rather than an HTTP error code.
- **No schema migrations.** `spring.jpa.hibernate.ddl-auto=update` — Hibernate derives and
  mutates the schema from the `@Entity` classes at startup; there is no Flyway/Liquibase and no
  `schema.sql`. `docs/schema.md` in this repo is the closest thing to a schema of record and
  should be kept in sync with `Models/`.
- **`spring-boot-starter-security` is a dependency with no `SecurityFilterChain` bean.** Only a
  `BCryptPasswordEncoder` bean is defined, so default Spring Security auto-configuration may be
  active on all endpoints — verify before assuming an endpoint is open.
- **Monetary fields use `float`.** `Booking.amount`, `Payment.amount`, and `Show_Seat.price` are
  all `float`; `review-rules.yaml` in this repo flags new `float`/`double` money fields as a
  finding.
