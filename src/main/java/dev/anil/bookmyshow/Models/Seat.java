package dev.anil.bookmyshow.Models;

import dev.anil.bookmyshow.Models.enums.SeatType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Seat extends BaseModel {

    private String seatNumber;
    private SeatType seatType;

    @ManyToOne
    private Screen screen;

    private int rowNo;
    private int columnNo;

}
