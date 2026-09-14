package dev.anil.bookmyshow.dtos;

import dev.anil.bookmyshow.Models.Booking;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelBookingResponseDto {
    private Booking booking;
    private ResponseStatus status;
}
