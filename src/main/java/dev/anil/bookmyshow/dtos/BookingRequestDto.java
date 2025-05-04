package dev.anil.bookmyshow.dtos;

import dev.anil.bookmyshow.Models.Show;
import dev.anil.bookmyshow.Models.Show_Seat;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookingRequestDto {

    private long userId;
    private long showId;
    private List<Long> showSeats;
}
