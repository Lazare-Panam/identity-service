package com.centralised.identity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

// What a customer may send from the sign-up form, and what each field must look like.
// Sizes match the app_user columns in V1__users.sql.
@Getter
@Setter
public class RegisterForm {

    @NotBlank(message = "Please enter your email")
    @Email(message = "Please enter a valid email address")
    @Size(max = 320, message = "Email is too long")
    private String email;

    @NotBlank(message = "Please choose a password")
    @Size(min = 10, max = 100, message = "Password must be at least 10 characters")
    private String password;

    @NotBlank(message = "Please enter your first name")
    @Size(max = 100, message = "First name is too long")
    private String firstName;

    @NotBlank(message = "Please enter your last name")
    @Size(max = 100, message = "Last name is too long")
    private String lastName;

    // Optional fields: may be left empty, but not too long
    @Size(max = 150, message = "Company name is too long")
    private String company;

    @Size(max = 100, message = "Job title is too long")
    private String jobTitle;

    @Size(max = 100, message = "Country is too long")
    private String country;
}
