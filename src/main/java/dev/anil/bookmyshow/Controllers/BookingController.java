package dev.anil.bookmyshow.Controllers;

import dev.anil.bookmyshow.Models.Booking;
import dev.anil.bookmyshow.Services.BookingService;
import dev.anil.bookmyshow.dtos.BookingRequestDto;
import dev.anil.bookmyshow.dtos.BookingResponseDto;
import dev.anil.bookmyshow.dtos.ResponseStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class BookingController {

    private BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/book")
    public BookingResponseDto Book(@RequestBody BookingRequestDto bookingRequestDto) {
        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        try {
            Booking booking = bookingService.book(bookingRequestDto.getUserId(), bookingRequestDto.getShowId(),
                    bookingRequestDto.getShowSeats());
            bookingResponseDto.setBooking(booking);
            bookingResponseDto.setStatus(ResponseStatus.SUCCESS);
        }
        catch (Exception e) {
            bookingResponseDto.setStatus(ResponseStatus.FAILURE);
        }
        return bookingResponseDto;

    }
}
