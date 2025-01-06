package com.example.LMS.service.impl;

import com.example.LMS.constant.PredefinedRole;
import com.example.LMS.dto.request.LoginRequest;
import com.example.LMS.dto.request.RegisterRequest;
import com.example.LMS.dto.response.LoginResponse;
import com.example.LMS.entity.User;
import com.example.LMS.exception.AuthenticationFailedException;
import com.example.LMS.exception.UserNotFoundException;
import com.example.LMS.mapper.UserMapper;
import com.example.LMS.repository.UserRepository;
import com.example.LMS.service.AuthenticationService;
import com.example.LMS.service.JwtService;
import com.fasterxml.classmate.AnnotationOverrides;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationServiceIml implements AuthenticationService {
    UserRepository userRepository;
    AuthenticationManager authenticationManager;
    JwtService jwtService;
    UserMapper userMapper;

    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UserNotFoundException("User not found"));
            String accessToken = jwtService.generateToken(user);
            return LoginResponse.builder().user(userMapper.toUserResponse(user)).accessToken(accessToken).build();
        } catch (Exception e) {
            throw new AuthenticationFailedException("Authentication failed");
        }
    }

    @Override
    public void register(RegisterRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .name(request.getName())
//                .roles(Set.of(PredefinedRole.USER_ROLE))
                .build();
        userRepository.save(user);
    }
}
