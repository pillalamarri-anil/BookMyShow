package dev.anil.bookmyshow.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Theater extends BaseModel {
    private String name;

    @OneToMany(mappedBy = "theater")
    private List<Screen> screenList;
}
