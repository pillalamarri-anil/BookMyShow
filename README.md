# BookMyShow

A Spring Boot movie-ticket booking service (BookMyShow clone).

## Tech stack

- Java / Spring Boot 3.4.5
- Spring Data JPA
- Spring Web
- Maven

## Running locally

```bash
./mvnw spring-boot:run
```

## Running tests

```bash
./mvnw test
```

## Project layout

| Package        | Responsibility                              |
| -------------- | ------------------------------------------- |
| `Controllers`  | REST endpoints                              |
| `Services`     | Business logic (booking, payment, sign-up)  |
| `Repositories` | Spring Data JPA repositories                |
| `Models`       | JPA entities and enums                      |
| `dtos`         | Request/response payloads                   |
| `Exceptions`   | Domain-specific exceptions                  |
