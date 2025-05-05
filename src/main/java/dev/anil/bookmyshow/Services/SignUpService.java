package dev.anil.bookmyshow.Services;

import dev.anil.bookmyshow.Exceptions.UserAlreadyExistException;
import dev.anil.bookmyshow.Models.User;
import dev.anil.bookmyshow.Repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Service
public class SignUpService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public SignUpService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/signup")
    public User signup(String userName, String password, String email, String phoneNumber) {

        if (userRepository.existsUserByMobile(phoneNumber))
            throw new UserAlreadyExistException("User with phone number already exists");

       if(userRepository.existsByEmail(email))
           throw new UserAlreadyExistException("User with email already exists");

       User user = new User();
       user.setEmail(email);
       user.setPassword(bCryptPasswordEncoder.encode(password));
       user.setName(userName);
       user.setMobile(phoneNumber);
       userRepository.save(user);
       return user;
    }
}
