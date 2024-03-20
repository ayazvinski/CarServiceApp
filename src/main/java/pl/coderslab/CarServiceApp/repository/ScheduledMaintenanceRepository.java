package pl.coderslab.CarServiceApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.CarServiceApp.entities.ScheduledMaintenance;

public interface ScheduledMaintenanceRepository extends JpaRepository<ScheduledMaintenance, Long> {
}

