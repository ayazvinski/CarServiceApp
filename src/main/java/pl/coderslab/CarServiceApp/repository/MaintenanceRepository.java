package pl.coderslab.CarServiceApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.CarServiceApp.entities.Maintenance;

public interface MaintenanceRepository extends JpaRepository <Maintenance, Long>{

}
