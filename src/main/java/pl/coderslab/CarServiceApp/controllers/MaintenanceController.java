package pl.coderslab.CarServiceApp.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.coderslab.CarServiceApp.entities.Maintenance;
import pl.coderslab.CarServiceApp.services.MaintenanceService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/maintenance")
public class MaintenanceController {
    public final MaintenanceService maintenanceService;

    @GetMapping("/add")
    public String showRegistrationForm(Model model) {
        Maintenance maintenance = new Maintenance();
        model.addAttribute("maintenance", maintenance);
        return "addMaintenance";
    }

    @PostMapping("/add")
    public String createMaintenance(@ModelAttribute Maintenance maintenance, BindingResult result) {
        try {
            maintenanceService.save(maintenance);
        } catch (DataIntegrityViolationException e) {
            result.rejectValue("name", "name.error", "Maintenance with this name already exists.");
            return "addMaintenance";
        }
        return "redirect:/maintenance/add";
    }

@GetMapping("/all")
public String showAllMaintenances(Model model) {
    List<Maintenance> allMaintenances = maintenanceService.findAll();
    model.addAttribute("maintenances", allMaintenances);
    return "allMaintenances";
}

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Maintenance maintenance = maintenanceService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid maintenance Id:" + id));
        model.addAttribute("maintenance", maintenance);
        return "editMaintenance";
    }

    @PostMapping("/edit")
    public String editMaintenance(@ModelAttribute Maintenance maintenance, RedirectAttributes redirectAttributes) {
        maintenanceService.save(maintenance);
        redirectAttributes.addFlashAttribute("successMessage", "Maintenance updated successfully!");
        return "redirect:/maintenance/all";
    }

    @GetMapping("/delete/{id}")
    public String deleteMaintenance(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            maintenanceService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Maintenance deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting maintenance.");
        }
        return "redirect:/maintenance/all";
    }


}
