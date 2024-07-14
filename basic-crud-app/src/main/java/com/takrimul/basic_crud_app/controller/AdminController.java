package com.takrimul.basic_crud_app.controller;

import com.takrimul.basic_crud_app.model.User;
import com.takrimul.basic_crud_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;

    @Autowired
    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "user-list";
    }

    @GetMapping("/user/name/{name}")
    public String getUserByName(@PathVariable String name, Model model) {
        Optional<User> userOptional = userRepository.findByName(name);
        userOptional.ifPresent(user -> model.addAttribute("user", user));
        return "user-details"; // Thymeleaf template to show user details
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, Model model) {
        userRepository.deleteById(id);
        model.addAttribute("message", "Deleted successfully");
        return "delete-message";
    }
}

