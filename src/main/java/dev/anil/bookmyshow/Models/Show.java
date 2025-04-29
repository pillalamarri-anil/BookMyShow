package dev.anil.bookmyshow.Models;

import dev.anil.bookmyshow.Models.enums.Feature;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.List;

@Entity(name="shows")
@Getter
@Setter
public class Show extends BaseModel{

    @ManyToOne
    private Movie movie;
    private long StartTime;
    private long EndTime;

    @ManyToOne
    private Screen screen;

    @Enumerated(EnumType.ORDINAL)
    @ElementCollection
    private List<Feature> features;
}
