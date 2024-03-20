package pl.coderslab.CarServiceApp.entities;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ScheduledMaintenanceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "scheduled_maintenance_id")
    private ScheduledMaintenance scheduledMaintenance;

    @ManyToOne
    @JoinColumn(name = "maintenance_id")
    private Maintenance maintenance;
}
