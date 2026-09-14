# Persistence schema

There is no `schema.sql`/Flyway/Liquibase — `spring.jpa.hibernate.ddl-auto=update`, so this
document is derived from (and must stay in sync with) the `@Entity` classes in
`src/main/java/dev/anil/bookmyshow/Models/`. Every entity extends `BaseModel`:

| column        | type   | notes                              |
| ------------- | ------ | ----------------------------------- |
| `id`          | bigint | PK, `GenerationType.AUTO`           |
| `created_at`  | bigint | epoch millis, app-managed           |
| `modified_at` | bigint | epoch millis, app-managed           |

Enum fields are `@Enumerated(EnumType.ORDINAL)` (persisted as `int`) unless noted otherwise —
adding/reordering enum constants is a breaking schema change.

## user
| column     | type         | notes        |
| ---------- | ------------ | ------------ |
| `name`     | varchar      |              |
| `mobile`   | varchar      | checked unique in code (`UserRepository.existsUserByMobile`), not DB-enforced |
| `email`    | varchar      | checked unique in code (`UserRepository.existsByEmail`), not DB-enforced |
| `password` | varchar      | BCrypt hash, written by `SignUpService` |

## booking
| column     | type          | notes                                          |
| ---------- | ------------- | ----------------------------------------------- |
| `user_id`  | bigint        | FK -> `user(id)`                                |
| `amount`   | float         | money stored as `float` — should be `BigDecimal`/`numeric` |
| `status`   | int           | ordinal of `BookingStatus` {PENDING, CONFIRMED} |

## booking_show_seat  (join table, unidirectional `@ManyToMany` on `Booking.seat`)
`booking_id bigint`, `show_seat_id bigint` — no `mappedBy` on the `Show_Seat` side.

## payment
| column       | type   | notes                                              |
| ------------ | ------ | --------------------------------------------------- |
| `booking_id` | bigint | FK -> `booking(id)` (`@ManyToOne`)                   |
| `ref_number` | bigint |                                                       |
| `amount`     | float  | money stored as `float` — should be `BigDecimal`/`numeric` |
| `status`     | int    | ordinal of `PaymentStatus` {PENDING, COMPLETED}      |

`Booking.paymentList` is the inverse side (`@OneToMany(mappedBy = "booking")`) — one booking can
have multiple payment attempts.

## shows  (entity mapped via `@Entity(name = "shows")`)
| column        | type   | notes                                                          |
| ------------- | ------ | ---------------------------------------------------------------- |
| `movie_id`    | bigint | FK -> `movie(id)`                                                 |
| `screen_id`   | bigint | FK -> `screen(id)`                                                |
| `start_time`  | bigint | epoch; field is `Show.StartTime` (PascalCase, unlike other fields) |
| `end_time`    | bigint | epoch; field is `Show.EndTime`                                     |

`features` is `@ElementCollection @Enumerated(ORDINAL) List<Feature>` — stored in a separate
element-collection table (default name `shows_features`) with ordinals of
`{TWO_D, THREE_D, IMAX, DOLBY, FOUR_D}`.

## show_seat
| column     | type   | notes                                                  |
| ---------- | ------ | -------------------------------------------------------- |
| `show_id`  | bigint | FK -> `shows(id)`                                         |
| `seat_id`  | bigint | FK -> `seat(id)`                                          |
| `status`   | int    | ordinal of `SeatStatus` {AVAILABLE, FILLED, BLOCKED}      |
| `price`    | float  | money stored as `float` — should be `BigDecimal`/`numeric` |

`Show_SeatRepository.findAllByIdAndStatus` takes a `@Lock(PESSIMISTIC_WRITE)` on rows during
booking; `updateShowSeats` is a bulk `@Modifying` update run inside the caller's transaction.

## seat
| column       | type    | notes                                       |
| ------------ | ------- | -------------------------------------------- |
| `screen_id`  | bigint  | FK -> `screen(id)`                            |
| `seat_number`| varchar |                                                |
| `seat_type`  | int     | ordinal of `SeatType` {PLATINUM, GOLD, SILVER} — no explicit `@Enumerated`, defaults to ordinal |
| `row_no`     | int     |                                                |
| `column_no`  | int     |                                                |

## screen
| column      | type    | notes                     |
| ----------- | ------- | -------------------------- |
| `theater_id`| bigint  | FK -> `theater(id)`         |
| `name`      | varchar |                             |

`Screen.seats` is the inverse side (`@OneToMany(mappedBy = "screen")`).

## theater
| column | type    | notes |
| ------ | ------- | ----- |
| `name` | varchar |       |

`Theater.screenList` is the inverse side (`@OneToMany(mappedBy = "theater")`).

## city_theater  (join table, unidirectional `@OneToMany` on `City.theaterList`)
`City.theaterList` has **no `mappedBy`** and `Theater` has no back-reference field, so Hibernate
generates an implicit join table (approx. `city_id`/`theater_id`) rather than a FK column on
`theater` — inconsistent with the `Theater`↔`Screen` and `Screen`↔`Seat` relationships, which are
bidirectional with `mappedBy`. Verify actual generated column names at runtime since `ddl-auto`
derives them.

## movie
| column | type    | notes |
| ------ | ------- | ----- |
| `name` | varchar |       |

## movie_actor  (join table, unidirectional `@ManyToMany` on `Movie.actorList`)
`movie_id bigint`, `actor_id bigint`.

## actor
| column | type    | notes |
| ------ | ------- | ----- |
| `name` | varchar |       |

## Cross-cutting notes

- `BookingRepository` is the only intended writer of `booking` — note that `BookingService.book()`
  currently constructs/mutates a `Booking` in memory but the visible code path does not appear to
  call `bookingRepository.save(booking)`; flag if a diff relies on the booking having been
  persisted.
- Every money field (`booking.amount`, `payment.amount`, `show_seat.price`) is `float`. Per
  `review-rules.yaml`, new or changed monetary fields should be `BigDecimal`, not `float`/`double`.
- No repository exists yet for `movie`, `theater`, `screen`, `seat`, `city`, `actor`, or `payment` —
  only `booking`, `shows`, `show_seat`, and `user` have a `Repository` today.
