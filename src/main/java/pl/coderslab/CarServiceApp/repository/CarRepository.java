package pl.coderslab.CarServiceApp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.CarServiceApp.entities.Car;
import pl.coderslab.CarServiceApp.entities.User;

import java.util.List;

public interface CarRepository extends JpaRepository <Car, Long> {
    List<Car> findByUser(User user);

}
