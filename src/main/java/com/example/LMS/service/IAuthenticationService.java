package com.example.LMS.service;

import com.example.LMS.constant.AccountProvider;
import com.example.LMS.dto.request.LoginRequest;
import com.example.LMS.dto.request.RegisterRequest;
import com.example.LMS.dto.response.LoginResponse;
import com.example.LMS.entity.User;

public interface IAuthenticationService {
    LoginResponse login(LoginRequest request);
    User register(RegisterRequest request, String avatar, AccountProvider provider, String providerId);
}
