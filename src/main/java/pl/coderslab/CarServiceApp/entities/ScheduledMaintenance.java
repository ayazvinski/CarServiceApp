package pl.coderslab.CarServiceApp.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class ScheduledMaintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @OneToMany(mappedBy = "scheduledMaintenance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduledMaintenanceItem> items = new ArrayList<>();

}
