package pl.coderslab.CarServiceApp.repository;


import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.CarServiceApp.entities.Car;

import java.util.List;
import java.util.Optional;


public interface CarRepository extends JpaRepository <Car, Long> {

}
