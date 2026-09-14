package dev.anil.bookmyshow.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelBookingRequestDto {

    private long userId;
    private long bookingId;
}
