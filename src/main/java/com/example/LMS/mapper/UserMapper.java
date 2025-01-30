package com.example.LMS.mapper;

import com.example.LMS.dto.response.InstructorResponse;
import com.example.LMS.dto.response.UserResponse;
import com.example.LMS.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.example.LMS.entity.Role;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);

    InstructorResponse toInstructorResponse(User user);

    default Set<String> mapRolesToRoleNames(Set<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
}
