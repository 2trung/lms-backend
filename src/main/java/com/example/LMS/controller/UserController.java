package com.example.LMS.controller;

import com.example.LMS.dto.SuccessResponse;
import com.example.LMS.dto.response.UserResponse;
import com.example.LMS.service.UserService;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@PreAuthorize("hasRole('USER')")
public class UserController {
    UserService userService;

    @GetMapping()
    public SuccessResponse<UserResponse> getUser() {
        return new SuccessResponse<>(200, "User detail retrieved successfully", userService.getUser());
    }

}
