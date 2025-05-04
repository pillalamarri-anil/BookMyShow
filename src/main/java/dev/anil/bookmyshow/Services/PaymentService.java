package dev.anil.bookmyshow.Services;

import dev.anil.bookmyshow.Models.Booking;
import dev.anil.bookmyshow.Models.Show_Seat;
import dev.anil.bookmyshow.Models.enums.PaymentStatus;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    public PaymentStatus pay(float amount) {
        return PaymentStatus.COMPLETED;
    }
}
