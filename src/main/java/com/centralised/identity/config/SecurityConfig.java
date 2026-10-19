package com.centralised.identity.config;

import com.centralised.identity.entity.AppUser;
import com.centralised.identity.repository.AppUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
public class SecurityConfig {

    // Hashes passwords as "{bcrypt}..." and checks them at login
    @Bean
    public PasswordEncoder encoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // Called by Spring Security at login: "who is this email?"
    // Brand is fixed to kosaplus for now; I2b takes it from the client_id
    @Bean
    public UserDetailsService userDetailsService(AppUserRepository users) {
        return email -> {
            AppUser appUser = users.findByBrand_CodeAndEmail("kosaplus", email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            boolean locked = appUser.getLockedUntil() != null
                    && appUser.getLockedUntil().isAfter(LocalDateTime.now());

            // Spring's own User object: Spring then checks the typed password against the hash
            return User.withUsername(appUser.getEmail())
                    .password(appUser.getPasswordHash())
                    .disabled(!appUser.isEnabled())
                    .accountLocked(locked)
                    .build();
        };
    }
}
