package com.example.LMS.controller;

import com.example.LMS.dto.SuccessResponse;
import com.example.LMS.dto.request.LoginRequest;
import com.example.LMS.dto.request.RegisterRequest;
import com.example.LMS.dto.request.SocialLoginRequest;
import com.example.LMS.dto.response.LoginResponse;
import com.example.LMS.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RequestMapping("/auth")
@RestController
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
public class AuthenticationController {
    AuthenticationService authenticationService;

    @GetMapping("/social-login")
    public SuccessResponse<?> generateLoginUrl(@RequestParam(name = "provider") String provider) {
        return new SuccessResponse<>(HttpStatus.OK.value(), "Successful", authenticationService.generateLoginUrl(provider));
    }

    @PostMapping("/social-login")
    public SuccessResponse<LoginResponse> socialAuthentication(@Validated @RequestBody SocialLoginRequest request) {
        var response = authenticationService.socialAuthentication(request.getCode(), request.getProvider());
        return new SuccessResponse<>(HttpStatus.OK.value(), "Login successful", response);
    }

    @PostMapping("/login")
    public SuccessResponse<LoginResponse> login(@Validated @RequestBody LoginRequest request) {
        var response = authenticationService.login(request);
        return new SuccessResponse<>(HttpStatus.OK.value(), "Login successful", response);
    }

    @PostMapping("/register")
    public SuccessResponse<String> register(@Validated @RequestBody RegisterRequest request) {
        authenticationService.register(request);
        return new SuccessResponse<>(HttpStatus.OK.value(), "Register successful");
    }
}
