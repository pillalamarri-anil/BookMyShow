package dev.anil.bookmyshow.dtos;

import dev.anil.bookmyshow.Models.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpResponseDto {
    private ResponseStatus status;
    private User user;
}
