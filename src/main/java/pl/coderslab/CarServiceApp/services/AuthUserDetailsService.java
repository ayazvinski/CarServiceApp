package pl.coderslab.CarServiceApp.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.coderslab.CarServiceApp.entities.User;
import pl.coderslab.CarServiceApp.repository.UserRepository;
import pl.coderslab.CarServiceApp.security.AuthUserDetails;

import java.util.Optional;

@RequiredArgsConstructor
public class AuthUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> byEmail = userRepository.findByEmail(username);

        return byEmail.map(user -> new AuthUserDetails(user))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}

