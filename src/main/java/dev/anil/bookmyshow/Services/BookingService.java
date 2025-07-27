package dev.anil.bookmyshow.Services;

import dev.anil.bookmyshow.Exceptions.PaymentNotCompletedException;
import dev.anil.bookmyshow.Exceptions.SeatNotAvailableException;
import dev.anil.bookmyshow.Models.Booking;
import dev.anil.bookmyshow.Models.Show;
import dev.anil.bookmyshow.Models.Show_Seat;
import dev.anil.bookmyshow.Models.User;
import dev.anil.bookmyshow.Models.enums.BookingStatus;
import dev.anil.bookmyshow.Models.enums.PaymentStatus;
import dev.anil.bookmyshow.Models.enums.SeatStatus;
import dev.anil.bookmyshow.Repositories.BookingRepository;
import dev.anil.bookmyshow.Repositories.ShowRepository;
import dev.anil.bookmyshow.Repositories.Show_SeatRepository;
import dev.anil.bookmyshow.Repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookingService {

    private UserRepository userRepository;
    private ShowRepository showRepository;
    private Show_SeatRepository show_SeatRepository;
    private BookingRepository bookingRepository;
    private PriceCalculationService priceCalculationService;
    private PaymentService paymentService;

    public BookingService(UserRepository userRepository, ShowRepository showRepository,
                          Show_SeatRepository show_SeatRepository, BookingRepository bookingRepository,
                          PriceCalculationService priceCalculationService, PaymentService paymentService) {
        this.userRepository = userRepository;
        this.showRepository = showRepository;
        this.show_SeatRepository = show_SeatRepository;
        this.bookingRepository = bookingRepository;
        this.priceCalculationService = priceCalculationService;
        this.paymentService = paymentService;
    }

    @Transactional(isolation= Isolation.SERIALIZABLE)
    public Booking book(long userId, long showId, List<Long> showSeatIds)
    {
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("User Not Found"));
        Show show = showRepository.findById(showId).orElseThrow(()->new RuntimeException("Show Not Found"));
        List<Show_Seat> show_Seats = show_SeatRepository.findAllByIdAndStatus(showSeatIds, SeatStatus.AVAILABLE);
        if(show_Seats.size() != showSeatIds.size())
            throw new SeatNotAvailableException("One or more Seats Not available");

        show_SeatRepository.updateShowSeats(show_Seats, SeatStatus.BLOCKED);

        float amount = priceCalculationService.calculcatePrice(show_Seats);
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setAmount(amount);
        booking.setSeat(show_Seats);
        booking.setStatus(BookingStatus.PENDING);

        // make payment
        PaymentStatus paymentStatus = paymentService.pay(amount);
        if(paymentStatus != PaymentStatus.COMPLETED)
            throw new PaymentNotCompletedException("Payment Not Completed");

        booking.setStatus(BookingStatus.CONFIRMED);
        return booking;
    }

}
