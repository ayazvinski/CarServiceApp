package pl.coderslab.CarServiceApp.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.coderslab.CarServiceApp.entities.*;
import pl.coderslab.CarServiceApp.services.CarService;
import pl.coderslab.CarServiceApp.services.MaintenanceService;
import pl.coderslab.CarServiceApp.services.ScheduledMaintenanceService;
import pl.coderslab.CarServiceApp.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/scheduled/maintenance")
public class ScheduledMaintenanceController {
    private final ScheduledMaintenanceService scheduledMaintenanceService;
    private final MaintenanceService maintenanceService;
    private final UserService userService;
    private final CarService carService;

    @GetMapping("/add")
    public String showAddForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByEmail(username).orElse(null);

        if (user != null) {
            List<Car> cars = carService.findCarsByUser(user);
            model.addAttribute("scheduledMaintenance", new ScheduledMaintenance());
            model.addAttribute("cars", cars);
            model.addAttribute("allMaintenances", maintenanceService.findAll());
            return "addScheduledMaintenance"; //
        } else {
            model.addAttribute("error", "User not found");
            return "addScheduledMaintenance"; // Nazwij widok, który ma być wyświetlany, gdy użytkownik nie zostanie znaleziony
        }
    }

    @PostMapping("/add")
    public String saveScheduledMaintenance(@ModelAttribute ScheduledMaintenance scheduledMaintenance,
                                           @RequestParam List<Long> maintenanceIds,
                                           BindingResult result,
                                           RedirectAttributes redirectAttributes) {
        if (!result.hasErrors()) {
            // Tworzenie ScheduledMaintenanceItem dla każdego wybranego Maintenance
            List<ScheduledMaintenanceItem> items = maintenanceIds.stream()
                    .map(id -> {
                        Maintenance maintenance = maintenanceService.findById(id).orElseThrow();
                        ScheduledMaintenanceItem item = new ScheduledMaintenanceItem();
                        item.setMaintenance(maintenance);
                        item.setScheduledMaintenance(scheduledMaintenance);
                        return item;
                    })
                    .collect(Collectors.toList());

            scheduledMaintenance.setItems(items);
            scheduledMaintenanceService.save(scheduledMaintenance);

            redirectAttributes.addFlashAttribute("successMessage", "Scheduled maintenance saved successfully!");
            return "redirect:/scheduled/maintenance/add";
        }
        return "addScheduledMaintenance";
    }


}
