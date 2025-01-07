package com.example.LMS.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class RegisterRequest {
    @Email(message = "Email is not valid")
    String email;

    @NotNull(message = "Password is required")
    String password;

    @NotNull(message = "Name is required")
    String name;
}
