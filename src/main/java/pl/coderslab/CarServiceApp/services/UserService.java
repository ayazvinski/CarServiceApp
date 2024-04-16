package pl.coderslab.CarServiceApp.services;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.coderslab.CarServiceApp.entities.User;
import pl.coderslab.CarServiceApp.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    public Optional<User> findByEmail(String username){
        return userRepository.findByEmail(username);
    }

    public String getAccessTokenForUser(User user) {
        // Assuming the accessToken field is always up-to-date and valid
        return user.getAccessToken(); // Simply return the access token
    }

    public void saveUserAccessToken(String username, String accessToken) {
        User user = userRepository.findByEmail(username).orElse(null);
        if (user != null) {
            user.setAccessToken(accessToken);
            userRepository.save(user);
        }
    }



}
