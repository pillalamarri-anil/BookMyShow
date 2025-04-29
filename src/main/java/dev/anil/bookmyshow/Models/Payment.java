package dev.anil.bookmyshow.Models;

import dev.anil.bookmyshow.Models.enums.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Payment extends BaseModel {

    @ManyToOne
    private Booking booking;

    long refNumber;

    float amount;

    @Enumerated(EnumType.ORDINAL)
    private PaymentStatus status;

}
