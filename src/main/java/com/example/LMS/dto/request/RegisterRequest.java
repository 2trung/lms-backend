package com.example.LMS.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Builder
public class RegisterRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    String email;

    @NotBlank(message = "Password is required")
    String password;

    @NotBlank(message = "Name is required")
    String name;
}
