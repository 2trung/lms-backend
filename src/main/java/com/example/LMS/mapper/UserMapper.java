package com.example.LMS.mapper;

import com.example.LMS.dto.response.UserResponse;
import com.example.LMS.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);
}
