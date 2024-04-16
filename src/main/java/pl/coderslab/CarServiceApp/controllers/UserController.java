package pl.coderslab.CarServiceApp.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.coderslab.CarServiceApp.entities.User;
import pl.coderslab.CarServiceApp.services.UserService;


@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;  // Generally, you won't need the client secret for building the authorization URI

    @Value("${spring.security.oauth2.client.registration.google.scope}")
    private String scope;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    @GetMapping("/get-token")
    public ModelAndView getToken() {
        String authorizationUri = "https://accounts.google.com/o/oauth2/auth" +
                "?client_id=" + clientId +
                "&response_type=code" +
                "&scope=" + scope +
                "&redirect_uri=" + redirectUri +
                "&access_type=offline";

        return new ModelAndView("redirect:" + authorizationUri);
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register")
    public String createUser(@ModelAttribute User user) {
        userService.register(user);
        return "startPage";
    }


}






