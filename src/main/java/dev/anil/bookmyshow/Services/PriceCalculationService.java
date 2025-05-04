package dev.anil.bookmyshow.Services;

import dev.anil.bookmyshow.Models.Show_Seat;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceCalculationService {

    public  float calculcatePrice(List<Show_Seat> showSeats){

        float amount = 0L;

        for(Show_Seat showSeat:  showSeats){
            amount += showSeat.getPrice();
        }
        return amount;
    }
}
