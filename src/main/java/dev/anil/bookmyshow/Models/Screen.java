package dev.anil.bookmyshow.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Screen extends BaseModel {
    private String name;

    @OneToMany(mappedBy = "screen")
    private List<Seat> seats;

   @ManyToOne
   private Theater theater;

}
