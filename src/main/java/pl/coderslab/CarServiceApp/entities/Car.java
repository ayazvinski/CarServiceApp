package pl.coderslab.CarServiceApp.entities;

import jakarta.persistence.*;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @NotEmpty
    private String carId;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User user;
    private String brand;
    private String model;
    private int year;
    private int engineSize;
    private String engineType;
    private String color;

}
