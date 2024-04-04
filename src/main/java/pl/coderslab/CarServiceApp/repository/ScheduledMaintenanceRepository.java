package pl.coderslab.CarServiceApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.CarServiceApp.entities.Car;
import pl.coderslab.CarServiceApp.entities.ScheduledMaintenance;

import java.util.List;


public interface ScheduledMaintenanceRepository extends JpaRepository<ScheduledMaintenance, Long> {
    List<ScheduledMaintenance> findByCarIn(List<Car> cars);

}


