package dev.anil.bookmyshow.Controllers;

import dev.anil.bookmyshow.Models.User;
import dev.anil.bookmyshow.Services.SignUpService;
import dev.anil.bookmyshow.dtos.ResponseStatus;
import dev.anil.bookmyshow.dtos.SignUpRequestDto;
import dev.anil.bookmyshow.dtos.SignUpResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private SignUpService signUpService;

    @PostMapping("/signup")
    public SignUpResponseDto signup(@RequestBody SignUpRequestDto signUpRequestDto)
    {
        SignUpResponseDto signUpResponseDto = new SignUpResponseDto();
        try
        {
            User user = signUpService.signup(signUpRequestDto.getUsername(), signUpRequestDto.getPassword(),
                    signUpRequestDto.getEmail(), signUpRequestDto.getPhone());
            signUpResponseDto.setUser(user);
            signUpResponseDto.setStatus(ResponseStatus.SUCCESS);
        }
        catch (Exception e)
        {
            signUpResponseDto.setStatus(ResponseStatus.FAILURE);
        }
        return signUpResponseDto;
    }

}
