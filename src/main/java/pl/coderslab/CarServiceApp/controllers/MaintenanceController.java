package pl.coderslab.CarServiceApp.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.annotation.Secured;
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
    private final MaintenanceService maintenanceService;

    @GetMapping("/add")
    @Secured("ROLE_Admin") // Ensure only admins can access the add maintenance form
    public String showRegistrationForm(Model model) {
        Maintenance maintenance = new Maintenance();
        model.addAttribute("maintenance", maintenance);
        return "addMaintenance";
    }

    @PostMapping("/add")
    @Secured("ROLE_Admin") // Ensure only admins can add maintenance
    public String createMaintenance(@ModelAttribute Maintenance maintenance, BindingResult result, RedirectAttributes redirectAttributes) {
        try {
            maintenanceService.save(maintenance);
        } catch (DataIntegrityViolationException e) {
            result.rejectValue("name", "name.error", "Maintenance with this name already exists.");
            return "addMaintenance";
        }
        redirectAttributes.addFlashAttribute("successMessage", "Maintenance added successfully!");
        return "redirect:/maintenance/all";
    }

    @GetMapping("/all")
    @Secured("ROLE_Admin") // Ensure only admins can view all maintenances
    public String showAllMaintenances(Model model) {
        List<Maintenance> allMaintenances = maintenanceService.findAll();
        model.addAttribute("maintenances", allMaintenances);
        return "allMaintenances";
    }

    @GetMapping("/edit/{id}")
    @Secured("ROLE_Admin") // Ensure only admins can access the edit form
    public String showEditForm(@PathVariable Long id, Model model) {
        Maintenance maintenance = maintenanceService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid maintenance Id: " + id));
        model.addAttribute("maintenance", maintenance);
        return "editMaintenance";
    }

    @PostMapping("/edit")
    @Secured("ROLE_Admin") // Ensure only admins can edit maintenance
    public String editMaintenance(@ModelAttribute Maintenance maintenance, RedirectAttributes redirectAttributes) {
        maintenanceService.save(maintenance);
        redirectAttributes.addFlashAttribute("successMessage", "Maintenance updated successfully!");
        return "redirect:/maintenance/all";
    }

    @GetMapping("/delete/{id}")
    @Secured("ROLE_Admin") // Ensure only admins can delete maintenance
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

