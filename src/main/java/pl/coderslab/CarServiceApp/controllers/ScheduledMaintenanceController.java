package pl.coderslab.CarServiceApp.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.coderslab.CarServiceApp.entities.*;
import pl.coderslab.CarServiceApp.services.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/scheduled/maintenance")
public class ScheduledMaintenanceController {
    private final ScheduledMaintenanceService scheduledMaintenanceService;
    private final MaintenanceService maintenanceService;
    private final UserService userService;
    private final CarService carService;
    private final CalendarService calendarService;


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
            return "addScheduledMaintenance";
        }
    }

    @PostMapping("/add")
    public String saveScheduledMaintenance(@ModelAttribute ScheduledMaintenance scheduledMaintenance,
                                           @RequestParam List<Long> maintenanceIds,
                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time,
                                           BindingResult result,
                                           RedirectAttributes redirectAttributes) {
        if (!result.hasErrors()) {
            scheduledMaintenance.setDate(date);
            scheduledMaintenance.setTime(time);

            List<ScheduledMaintenanceItem> items = maintenanceIds.stream()
                    .map(id -> {
                        Maintenance maintenance = maintenanceService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Maintenance ID: " + id));
                        ScheduledMaintenanceItem item = new ScheduledMaintenanceItem();
                        item.setMaintenance(maintenance);
                        item.setScheduledMaintenance(scheduledMaintenance);
                        return item;
                    })
                    .collect(Collectors.toList());

            scheduledMaintenance.setItems(items);
            scheduledMaintenanceService.save(scheduledMaintenance);

            redirectAttributes.addFlashAttribute("successMessage", "Scheduled maintenance saved successfully!");
            return "redirect:/scheduled/maintenance/all";
        }
        return "addScheduledMaintenance";
    }

    @GetMapping("/all")
    public String listScheduledMaintenances(Model model, Authentication authentication) {
        String email = authentication.getName();
        List<ScheduledMaintenance> maintenances = scheduledMaintenanceService.findScheduledMaintenancesForCurrentUser(email);

        maintenances.sort(Comparator.comparing(ScheduledMaintenance::getDate)
                .thenComparing(ScheduledMaintenance::getTime));

        Map<Car, List<ScheduledMaintenance>> groupedByCar = maintenances.stream()
                .collect(Collectors.groupingBy(ScheduledMaintenance::getCar));

        model.addAttribute("groupedMaintenances", groupedByCar);
        return "allScheduledMaintenance";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        ScheduledMaintenance scheduledMaintenance = scheduledMaintenanceService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid scheduledMaintenance Id: " + id));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByEmail(username).orElse(null);

        if (user != null) {
            List<Car> cars = carService.findCarsByUser(user);
            List<Long> maintenanceIds = scheduledMaintenance.getItems().stream()
                    .map(item -> item.getMaintenance().getId())
                    .collect(Collectors.toList());

            String formattedDate = scheduledMaintenance.getDate().toString();
            String formattedTime = scheduledMaintenance.getTime().toString();

            model.addAttribute("formattedDate", formattedDate);
            model.addAttribute("formattedTime", formattedTime);

            model.addAttribute("scheduledMaintenance", scheduledMaintenance);
            model.addAttribute("cars", cars);
            model.addAttribute("allMaintenances", maintenanceService.findAll());
            model.addAttribute("maintenanceIds", maintenanceIds);
        } else {
            model.addAttribute("error", "User not found");
        }
        return "editScheduledMaintenance";
    }

    @PostMapping("/edit/{id}")
    public String updateScheduledMaintenance(@PathVariable Long id,
                                             @ModelAttribute ScheduledMaintenance scheduledMaintenance,
                                             @RequestParam List<Long> maintenanceIds,
                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time,
                                             BindingResult result,
                                             RedirectAttributes redirectAttributes) {
        if (!result.hasErrors()) {

            scheduledMaintenance.setDate(date);
            scheduledMaintenance.setTime(time);

            List<ScheduledMaintenanceItem> existingItems = scheduledMaintenance.getItems();

            existingItems.removeIf(item -> !maintenanceIds.contains(item.getMaintenance().getId()));

            List<Long> existingMaintenanceIds = existingItems.stream()
                    .map(item -> item.getMaintenance().getId())
                    .toList();

            List<Long> newMaintenanceIds = maintenanceIds.stream()
                    .filter(maintenanceId -> !existingMaintenanceIds.contains(id))
                    .toList();

            List<ScheduledMaintenanceItem> newItems = newMaintenanceIds.stream()
                    .map(maintenanceId -> {
                        Maintenance maintenance = maintenanceService.findById(maintenanceId)
                                .orElseThrow(() -> new IllegalArgumentException("Invalid Maintenance ID: " + maintenanceId));
                        ScheduledMaintenanceItem newItem = new ScheduledMaintenanceItem();
                        newItem.setMaintenance(maintenance);
                        newItem.setScheduledMaintenance(scheduledMaintenance);
                        return newItem;
                    }).toList();

            existingItems.addAll(newItems);

            scheduledMaintenance.setItems(existingItems);
            scheduledMaintenanceService.save(scheduledMaintenance);

            redirectAttributes.addFlashAttribute("successMessage", "Scheduled maintenance updated successfully!");
            return "redirect:/scheduled/maintenance/all";
        }

        return "editScheduledMaintenance";
    }


    @GetMapping("/delete/{id}")
    public String deleteScheduledMaintenance(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        ScheduledMaintenance scheduledMaintenance = scheduledMaintenanceService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid scheduledMaintenance Id:" + id));
        scheduledMaintenanceService.delete(scheduledMaintenance.getId());
        redirectAttributes.addFlashAttribute("successMessage", "Scheduled maintenance deleted successfully!");
        return "redirect:/scheduled/maintenance/all";
    }

    @PostMapping("/add-to-calendar/{id}")
    public String addToCalendar(@PathVariable Long id, RedirectAttributes redirectAttributes, Authentication authentication) {

        Logger logger = LoggerFactory.getLogger(ScheduledMaintenanceController.class);

        try {
            logger.debug("Finding scheduled maintenance with ID: {}", id);
            ScheduledMaintenance scheduledMaintenance = scheduledMaintenanceService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Scheduled Maintenance ID: " + id));

            logger.debug("Getting email from authentication");
            String email = authentication.getName();
            logger.debug("Finding user by email: {}", email);
            User user = userService.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));

            logger.debug("Retrieving access token for user: {}", user.getEmail());
            String accessToken = userService.getAccessTokenForUser(user);
            logger.debug("Access token: {}", accessToken);

            logger.debug("Adding event to Google Calendar");
            calendarService.createCalendarEvent(accessToken, scheduledMaintenance);

            redirectAttributes.addFlashAttribute("successMessage", "Event added to Google Calendar successfully!");
            logger.debug("Event added successfully");
        } catch (Exception e) {
            logger.error("Failed to add event to Google Calendar", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to add event to Google Calendar: " + e.getMessage());
        }
        return "redirect:/scheduled/maintenance/all";
    }
}