package pl.coderslab.CarServiceApp.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.coderslab.CarServiceApp.entities.Car;
import pl.coderslab.CarServiceApp.repository.CarRepository;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;

    public Car save(Car car) {
        return carRepository.save(car);
    }

}
