package pl.coderslab.CarServiceApp.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.coderslab.CarServiceApp.entities.Car;
import pl.coderslab.CarServiceApp.entities.ScheduledMaintenance;
import pl.coderslab.CarServiceApp.entities.User;
import pl.coderslab.CarServiceApp.repository.CarRepository;
import pl.coderslab.CarServiceApp.repository.ScheduledMaintenanceRepository;
import pl.coderslab.CarServiceApp.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduledMaintenanceService {
    private final ScheduledMaintenanceRepository scheduledMaintenanceRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;

    public List<ScheduledMaintenance> findAll() {
        return scheduledMaintenanceRepository.findAll();
    }
    public List<ScheduledMaintenance> findScheduledMaintenancesForCurrentUser(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Car> cars = carRepository.findByUser(user);
            return scheduledMaintenanceRepository.findByCarIn(cars);
        } else {
            return List.of();
        }
    }


    public Optional<ScheduledMaintenance> findById(Long id) {
        return scheduledMaintenanceRepository.findById(id);
    }

    public ScheduledMaintenance save(ScheduledMaintenance scheduledMaintenance) {
        return scheduledMaintenanceRepository.save(scheduledMaintenance);
    }

    public void delete(Long id) {
        scheduledMaintenanceRepository.deleteById(id);
    }

}

