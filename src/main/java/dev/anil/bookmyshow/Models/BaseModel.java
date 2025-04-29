package dev.anil.bookmyshow.Models;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@MappedSuperclass
public class BaseModel {
    @Id
    private long id;
    private long createdAt;
    private long modifiedAt;
}
