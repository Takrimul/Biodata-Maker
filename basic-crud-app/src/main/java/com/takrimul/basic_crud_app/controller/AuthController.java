package com.takrimul.basic_crud_app.controller;

import com.takrimul.basic_crud_app.model.RegisteredUser;
import com.takrimul.basic_crud_app.repository.RegisteredUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final RegisteredUserRepository registeredUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(RegisteredUserRepository registeredUserRepository, PasswordEncoder passwordEncoder) {
        this.registeredUserRepository = registeredUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registeredUser", new RegisteredUser());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute RegisteredUser registeredUser,
                               @RequestParam(defaultValue = "false") boolean isAdmin,
                               Model model) {
        registeredUser.setPassword(passwordEncoder.encode(registeredUser.getPassword()));

        // Set roles
        registeredUser.addRole("USER");
        if (isAdmin) {
            registeredUser.addRole("ADMIN");
        }

        registeredUserRepository.save(registeredUser);
        model.addAttribute("message", "Registration successful");
        return "result";
    }
}