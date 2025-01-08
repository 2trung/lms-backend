package com.example.LMS.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SocialLoginRequest {
    @NotBlank(message = "Code is required")
    String code;

    @NotBlank(message = "Provider is required")
    String provider;
}
