package dev.anil.bookmyshow.Repositories;

import dev.anil.bookmyshow.Models.Show_Seat;
import dev.anil.bookmyshow.Models.enums.SeatStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Show_SeatRepository extends JpaRepository<Show_Seat, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select ss from Show_Seat ss where ss.id IN :ids and ss.status = :status")
    List<Show_Seat> findAllByIdAndStatus(List<Long> ids, SeatStatus status);

    Show_Seat save(Show_Seat show_seat);

    @Query("update Show_Seat ss set ss.status = :status where ss IN :showSeats ")
    List<Show_Seat> updateShowSeats( List<Show_Seat> showSeats, SeatStatus status);

    Show_Seat findById(long id);
}
