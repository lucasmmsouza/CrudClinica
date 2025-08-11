package com.example.crudclinica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        // Esta linha diz ao Thymeleaf para procurar em "templates/error/access-denied.html"
        return "error/access-denied";
    }
}