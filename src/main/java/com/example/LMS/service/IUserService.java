package com.example.LMS.service;

import com.example.LMS.dto.response.UserResponse;
import org.springframework.stereotype.Service;

public interface IUserService {
    UserResponse getUser();
}
