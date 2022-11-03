package com.example.login;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HomeController {

    @GetMapping("/")
    String hello(@AuthenticationPrincipal OidcUser oidcUser) {
        return "Hello, " + oidcUser.getFullName();
    }
}
