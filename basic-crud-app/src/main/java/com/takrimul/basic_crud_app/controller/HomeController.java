package com.takrimul.basic_crud_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;


@Controller
public class HomeController {

    @GetMapping("/home")
    public String welcome(Model model) {
        model.addAttribute("message", "Welcome to Home Page");
        return "welcome";
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // Return the name of the Thymeleaf template for login page
    }
}
