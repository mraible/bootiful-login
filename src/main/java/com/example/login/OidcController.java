package com.example.login;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ClientRegistrations;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.GenericWebApplicationContext;

import javax.validation.Valid;

@RestController
public class OidcController {

    private final GenericWebApplicationContext context;

    public OidcController(GenericWebApplicationContext context) {
        this.context = context;
    }

    @PostMapping("/api/configure")
    public ResponseEntity<OidcSettings> configure(@Valid @RequestBody OidcSettings settings) {
        ClientRegistrationRepository registrationRepository =
            new InMemoryClientRegistrationRepository(clientRegistration(settings));

        // https://stackoverflow.com/a/63396727/65681
        AutowireCapableBeanFactory factory = context.getAutowireCapableBeanFactory();
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) factory;
        // System.out.println(Arrays.toString(registry.getBeanDefinitionNames()));
        registry.removeBeanDefinition("clientRegistrationRepository");
        context.registerBean("clientRegistrationRepository", ClientRegistrationRepository.class, () -> registrationRepository);
        return ResponseEntity.accepted().body(settings);
    }

    private ClientRegistration clientRegistration(OidcSettings settings) {
        return ClientRegistrations.fromIssuerLocation(settings.issuer())
            .clientId(settings.clientId())
            .clientSecret(settings.clientSecret())
            .build();
    }
}
