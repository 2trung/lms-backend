package com.example.LMS.service;

import com.example.LMS.dto.response.UserResponse;
import com.example.LMS.entity.User;
import com.example.LMS.mapper.UserMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserService implements IUserService {
    UserMapper userMapper;
    public UserResponse getUser() {
        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userMapper.toUserResponse(user);
    }
}
