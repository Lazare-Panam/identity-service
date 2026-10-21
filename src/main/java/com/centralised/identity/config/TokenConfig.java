package com.centralised.identity.config;

import com.centralised.identity.repository.AppUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.UUID;

@Configuration
public class TokenConfig {

    // Puts the customer's name and email into the ID token, so the website can say "Hi Alice"
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> idTokenCustomizer(AppUserRepository users) {
        return context -> {
            if (!OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
                return;
            }
            users.findById(UUID.fromString(context.getPrincipal().getName())).ifPresent(user -> context.getClaims()
                    .claim("email", user.getEmail())
                    .claim("given_name", user.getFirstName())
                    .claim("family_name", user.getLastName())
                    .claim("name", user.getFirstName() + " " + user.getLastName()));
        };
    }
}
