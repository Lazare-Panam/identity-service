package com.centralised.identity.controller;

import com.centralised.identity.dto.RegisterForm;
import com.centralised.identity.service.EmailAlreadyUsedException;
import com.centralised.identity.service.RegisterService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {
    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    // Remove spaces around the email before it is checked (the password is left exactly as typed)
    @InitBinder("registerForm")
    void trimEmail(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, "email", new StringTrimmerEditor(false));
    }

    @GetMapping("/register")
    public String form(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerForm") RegisterForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }
        try {
            registerService.register(form);
        } catch (EmailAlreadyUsedException e) {
            result.rejectValue("email", "taken", "An account with this email already exists. Sign in or reset your password.");
            return "register";
        }
        return "redirect:/login?registered";
    }
}
