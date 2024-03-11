package pl.coderslab.CarServiceApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.CarServiceApp.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Long> {
    Optional<User> findByEmail(String username);
}

