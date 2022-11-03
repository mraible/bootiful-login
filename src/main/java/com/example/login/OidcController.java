package com.example.login;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.GenericWebApplicationContext;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
public class OidcController {

    private final ClientRegistrationRepository repository;

    private final GenericWebApplicationContext context;

    public OidcController(ClientRegistrationRepository repository, GenericWebApplicationContext context) {
        this.repository = repository;
        this.context = context;
    }

    @PostMapping("/api/configure")
    public ResponseEntity<OidcSettings> configure(@Valid @RequestBody OidcSettings settings) {
        ClientRegistrationRepository registrationRepository = new InMemoryClientRegistrationRepository(clientRegistrationBuilder(settings).build());

        // https://stackoverflow.com/a/63396727/65681
        AutowireCapableBeanFactory factory = context.getAutowireCapableBeanFactory();
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) factory;
        // System.out.println(Arrays.toString(registry.getBeanDefinitionNames()));
        registry.removeBeanDefinition("clientRegistrationRepository");
        context.registerBean("clientRegistrationRepository", ClientRegistrationRepository.class, () -> registrationRepository);
        return ResponseEntity.accepted().body(settings);
    }

    private ClientRegistration.Builder clientRegistrationBuilder(OidcSettings settings) {
        Map<String, Object> metadata = new HashMap<>();

        // URIs from https://dev-06bzs1cu.us.auth0.com/.well-known/openid-configuration
        return ClientRegistration
            .withRegistrationId("auth0")
            .issuerUri(settings.issuer())
            .redirectUri("{baseUrl}/{action}/oauth2/code/{registrationId}")
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .scope("email")
            .authorizationUri(settings.issuer() + "authorize")
            .tokenUri(settings.issuer() + "token")
            .jwkSetUri(settings.issuer() + ".well-known/jwks.json")
            .userInfoUri(settings.issuer() + "userinfo")
            .providerConfigurationMetadata(metadata)
            .userNameAttributeName("email")
            .clientId(settings.clientId())
            .clientSecret(settings.clientSecret());
    }
}
