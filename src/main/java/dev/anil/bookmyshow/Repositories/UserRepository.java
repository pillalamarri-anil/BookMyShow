package dev.anil.bookmyshow.Repositories;

import dev.anil.bookmyshow.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserById(long id);

    boolean existsByEmail(String email);

    boolean existsUserByMobile(String mobile);
}
