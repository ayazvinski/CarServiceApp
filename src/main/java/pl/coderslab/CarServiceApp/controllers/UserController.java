package pl.coderslab.CarServiceApp.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.coderslab.CarServiceApp.entities.User;
import pl.coderslab.CarServiceApp.services.UserService;


@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register")
    public String createUser(@ModelAttribute User user) {
        userService.register(user);
        return "register";
    }

}






