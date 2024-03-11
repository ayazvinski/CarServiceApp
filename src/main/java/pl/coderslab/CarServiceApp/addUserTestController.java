package pl.coderslab.CarServiceApp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.coderslab.CarServiceApp.services.UserService;

@Controller
@RequiredArgsConstructor
public class addUserTestController {

    private final UserService userService;

    @GetMapping("/registerTest")
    @ResponseBody
    public pl.coderslab.CarServiceApp.entities.User register() {
        pl.coderslab.CarServiceApp.entities.User user = new pl.coderslab.CarServiceApp.entities.User();
        user.setEmail("a.yazvinski@gmail.com");
        user.setPassword("arturek");

        return userService.register(user);
    }

}

