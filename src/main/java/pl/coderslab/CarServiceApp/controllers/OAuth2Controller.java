package pl.coderslab.CarServiceApp.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.coderslab.CarServiceApp.services.UserService;
import org.springframework.security.core.Authentication;


import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class OAuth2Controller {

    private OAuth2AuthorizedClientService authorizedClientService;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private UserService userService;

    @GetMapping("/authorize")
    public ModelAndView redirectToGoogleAuthorization() {
        ClientRegistration registration = clientRegistrationRepository.findByRegistrationId("google");
        String scopes = registration.getScopes().stream()
                .collect(Collectors.joining(" "));

        System.out.println("Requesting scopes: " + scopes);

        String authorizationUri = OAuth2AuthorizationRequest.authorizationCode()
                .authorizationUri(registration.getProviderDetails().getAuthorizationUri())
                .clientId(registration.getClientId())
                .redirectUri(registration.getRedirectUri())
                .scope(scopes)
                .state("state")
                .build()
                .getAuthorizationRequestUri();

        return new ModelAndView("redirect:" + authorizationUri);
    }


    @GetMapping("/callback")
    public String callback(Authentication authentication) {

        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                "google", authentication.getName());

        if (authorizedClient != null) {
            String accessToken = authorizedClient.getAccessToken().getTokenValue();

            userService.saveUserAccessToken(authentication.getName(), accessToken);
        }

        return "redirect:/home";
    }
}