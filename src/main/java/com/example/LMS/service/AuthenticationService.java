package com.example.LMS.service;

import com.example.LMS.dto.request.LoginRequest;
import com.example.LMS.dto.request.RegisterRequest;
import com.example.LMS.dto.response.LoginResponse;

public interface AuthenticationService {
    LoginResponse login(LoginRequest request);
    void register(RegisterRequest request);
}
