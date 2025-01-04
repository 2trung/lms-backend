package com.example.LMS.dto;

import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonInclude;

@Getter
public class SuccessResponse<T> {
    private final int status;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public SuccessResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public SuccessResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
