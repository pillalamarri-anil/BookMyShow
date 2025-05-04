package dev.anil.bookmyshow.Repositories;

import dev.anil.bookmyshow.Models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {

   Booking save(Booking booking);
}
