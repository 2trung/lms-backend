package com.example.LMS.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class AuthenticationController {
    @GetMapping("/")
    public String home() {
        return "Homepage";
    }

    @GetMapping("/user")
    public Principal user(Principal user) {
        return user;
    }
}
