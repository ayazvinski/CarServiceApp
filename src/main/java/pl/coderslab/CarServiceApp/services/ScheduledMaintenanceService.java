package pl.coderslab.CarServiceApp.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.CarServiceApp.entities.Car;
import pl.coderslab.CarServiceApp.entities.Maintenance;
import pl.coderslab.CarServiceApp.entities.ScheduledMaintenance;
import pl.coderslab.CarServiceApp.entities.User;
import pl.coderslab.CarServiceApp.repository.MaintenanceRepository;
import pl.coderslab.CarServiceApp.repository.ScheduledMaintenanceRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduledMaintenanceService {
    private final ScheduledMaintenanceRepository repository;

    public List<ScheduledMaintenance> findAll() {
        return repository.findAll();
    }

    public Optional<ScheduledMaintenance> findById(Long id) {
        return repository.findById(id);
    }

    public ScheduledMaintenance save(ScheduledMaintenance scheduledMaintenance) {
        return repository.save(scheduledMaintenance);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}

