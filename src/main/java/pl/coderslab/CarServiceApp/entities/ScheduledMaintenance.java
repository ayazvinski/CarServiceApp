package pl.coderslab.CarServiceApp.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class ScheduledMaintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private LocalTime time;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @OneToMany(mappedBy = "scheduledMaintenance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduledMaintenanceItem> items = new ArrayList<>();

}
