package com.centralised.identity.service;

import com.centralised.identity.dto.RegisterForm;
import com.centralised.identity.entity.AppUser;
import com.centralised.identity.entity.Brand;
import com.centralised.identity.repository.AppUserRepository;
import com.centralised.identity.repository.BrandRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterService {
    private static final String BRAND = "kosaplus";

    private final AppUserRepository users;
    private final BrandRepository brands;
    private final PasswordEncoder encoder;

    public RegisterService(AppUserRepository users, BrandRepository brands, PasswordEncoder encoder) {
        this.users = users;
        this.brands = brands;
        this.encoder = encoder;
    }

    @Transactional
    public void register(RegisterForm form) {
        String email = form.getEmail().trim().toLowerCase();

        // One account per email on this brand
        if (users.existsByBrand_CodeAndEmail(BRAND, email)) {
            throw new EmailAlreadyUsedException();
        }

        Brand brand = brands.findByCode(BRAND).orElseThrow();

        AppUser user = new AppUser();
        user.setBrand(brand);
        user.setEmail(email);
        user.setPasswordHash(encoder.encode(form.getPassword()));
        user.setFirstName(form.getFirstName().trim());
        user.setLastName(form.getLastName().trim());
        user.setCompany(blankToNull(form.getCompany()));
        user.setJobTitle(blankToNull(form.getJobTitle()));
        user.setCountry(blankToNull(form.getCountry()));

        users.save(user);
    }

    // Optional fields left empty on the form are stored as NULL, not as ""
    private static String blankToNull(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }
}
