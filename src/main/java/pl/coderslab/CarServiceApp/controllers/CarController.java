package pl.coderslab.CarServiceApp.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.coderslab.CarServiceApp.entities.Car;
import pl.coderslab.CarServiceApp.entities.User;
import pl.coderslab.CarServiceApp.services.CarService;
import pl.coderslab.CarServiceApp.services.UserService;

import java.util.List;

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

        return "redirect:/car/all";
    }

    @GetMapping("/all")
    public String listUserCars(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByEmail(username).orElse(null);

        if (user != null) {
            List<Car> cars = carService.findCarsByUser(user);
            model.addAttribute("cars", cars);
        }

        return "allCars";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Car car = carService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid car Id:" + id));
        model.addAttribute("car", car);
        return "carEdit";
    }

    @PostMapping("/edit")
    public String processEditForm(@ModelAttribute Car car) {
        Car existingCar = carService.findById(car.getId()).orElseThrow(() -> new IllegalArgumentException("Invalid car Id:" + car.getId()));
        car.setUser(existingCar.getUser());
        carService.save(car);
        return "redirect:/car/all";
    }
    @GetMapping("/delete/{id}")
    public String deleteCar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            carService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Car was successfully deleted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting car.");
        }
        return "redirect:/car/all";
    }
}
