package pl.coderslab.CarServiceApp.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.CarServiceApp.entities.Car;
import pl.coderslab.CarServiceApp.entities.User;
import pl.coderslab.CarServiceApp.services.CarService;
import pl.coderslab.CarServiceApp.services.UserService;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/car")
public class CarController {

    public final CarService carService;
    public final UserService userService;

    @GetMapping("/add")
    public String showRegistrationForm(Model model) {
        Car car = new Car();
        model.addAttribute("car", car);
        return "addCar";
    }

    @PostMapping("/add")
    public String createCar(@ModelAttribute Car car) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        userService.findByEmail(currentPrincipalName).ifPresent(user -> {
            car.setUser(user);
            carService.save(car);
        });

        return "redirect:/hello";
    }
}
