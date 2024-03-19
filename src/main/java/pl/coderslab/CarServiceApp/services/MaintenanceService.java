package pl.coderslab.CarServiceApp.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.coderslab.CarServiceApp.entities.Maintenance;
import pl.coderslab.CarServiceApp.repository.MaintenanceRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MaintenanceService {
    private final MaintenanceRepository maintenanceRepository;

    public Maintenance save (Maintenance maintenance) {
        return maintenanceRepository.save(maintenance);
    }
    public Optional<Maintenance> findById (Long id){
        return maintenanceRepository.findById(id);
    }

    public List<Maintenance> findAll(){
        return maintenanceRepository.findAll();
    }
    public void delete(Long id) {
        maintenanceRepository.deleteById(id);
    }


}
