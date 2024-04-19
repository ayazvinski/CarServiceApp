package pl.coderslab.CarServiceApp.services;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.coderslab.CarServiceApp.entities.User;
import pl.coderslab.CarServiceApp.repository.UserRepository;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.scope}")
    private String scope;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    public User register(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    public Optional<User> findByEmail(String username){
        return userRepository.findByEmail(username);
    }

    public String getAccessTokenForUser(User user) {
        return user.getAccessToken();
    }

    public void saveUserAccessToken(String email, String accessToken) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setAccessToken(accessToken);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found with email: " + email);
        }
    }

    public String exchangeCodeForAccessToken(String code) {
        String url = "https://oauth2.googleapis.com/token";
        Map<String, String> requestBody = Map.of(
                "code", code,
                "client_id", clientId,
                "client_secret", clientSecret,
                "redirect_uri", redirectUri,
                "grant_type", "authorization_code"
        );

        try {
            Map<String, String> response = restTemplate.postForObject(url, requestBody, Map.class);
            return Optional.ofNullable(response)
                    .map(r -> r.get("access_token"))
                    .orElseThrow(() -> new RuntimeException("No access token in response"));
        } catch (HttpClientErrorException ex) {
            throw new RuntimeException("Failed to retrieve access token: " + ex.getResponseBodyAsString(), ex);
        }
    }

}
