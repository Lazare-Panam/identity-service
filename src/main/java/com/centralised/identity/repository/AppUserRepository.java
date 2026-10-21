package com.centralised.identity.repository;

import com.centralised.identity.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
    Optional<AppUser> findByBrand_CodeAndEmail(String brandCode, String email);
    boolean existsByBrand_CodeAndEmail(String brandCode, String email);
}
