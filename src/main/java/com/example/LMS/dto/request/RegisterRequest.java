package com.example.LMS.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class RegisterRequest {
    String email;
    String password;
    String name;
}
