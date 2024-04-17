package pl.coderslab.CarServiceApp.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.coderslab.CarServiceApp.entities.Car;
import pl.coderslab.CarServiceApp.entities.User;
import pl.coderslab.CarServiceApp.repository.CarRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;

    public Car save(Car car) {
        return carRepository.save(car);
    }

    public Optional<Car> findById(Long id) {
        return carRepository.findById(id);
    }

    public List<Car> findCarsByUser(User user) {
        return carRepository.findByUser(user);
    }
    public void delete(Long id) {
        carRepository.deleteById(id);
    }

}
