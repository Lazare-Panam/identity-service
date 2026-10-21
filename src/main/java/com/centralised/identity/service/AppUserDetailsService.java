package com.centralised.identity.service;

import com.centralised.identity.entity.AppUser;
import com.centralised.identity.repository.AppUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AppUserDetailsService implements UserDetailsService {
    private final AppUserRepository users;

    public AppUserDetailsService(AppUserRepository users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = users.findByBrand_CodeAndEmail("kosaplus", email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        boolean locked = appUser.getLockedUntil() != null
                && appUser.getLockedUntil().isAfter(LocalDateTime.now());

        // The username Spring keeps is the permanent user id: tokens carry it as "sub" (emails can change)
        return User.withUsername(appUser.getId().toString())
                .password(appUser.getPasswordHash())
                .disabled(!appUser.isEnabled())
                .accountLocked(locked)
                .build();
    }
}
