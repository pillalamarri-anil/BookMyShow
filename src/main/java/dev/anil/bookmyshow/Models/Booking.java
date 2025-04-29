package dev.anil.bookmyshow.Models;

import dev.anil.bookmyshow.Models.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Booking extends BaseModel{

    @ManyToMany
    private List<Show_Seat> seat; // considering cancellation scenario

    @OneToMany(mappedBy = "booking")
    private List<Payment> paymentList;

    @ManyToOne
    private User user;

    private float amount;

    @Enumerated(EnumType.ORDINAL)
    private BookingStatus status;

}
