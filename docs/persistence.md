---
applies_to: ["**/Repositories/**", "**/Models/**", "**/*Repository.java"]
---
# Persistence conventions

- Every repository interface is annotated `@Repository` and extends `JpaRepository<T, Long>`
  (see `BookingRepository`, `ShowRepository`, `Show_SeatRepository`, `UserRepository`).
- Custom finders return `Optional<T>` (e.g. `UserRepository.findUserById`) — never `null` — or a
  `List<T>` for multi-row lookups (e.g. `Show_SeatRepository.findAllByIdAndStatus`).
- Row-locking finders that feed a booking/reservation flow must declare
  `@Lock(LockModeType.PESSIMISTIC_WRITE)`, matching `Show_SeatRepository.findAllByIdAndStatus`.
- Bulk mutations (`@Modifying` queries) must run inside the caller's `@Transactional` boundary,
  not open their own — `Show_SeatRepository.updateShowSeats` relies on `BookingService.book()`'s
  `@Transactional(isolation = SERIALIZABLE)` wrapper.
- Entities are mutated only through their owning service, never directly from a controller or
  another entity's service.
- New entities should follow `BaseModel` for `id`/`createdAt`/`modifiedAt` rather than
  redeclaring an id field.
- Bidirectional associations should use `mappedBy` on the inverse side (as `Theater.screenList`
  and `Screen.seats` do) rather than leaving both sides as separate unidirectional mappings
  (`City.theaterList` does this today and is a known inconsistency — see `docs/schema.md`).
