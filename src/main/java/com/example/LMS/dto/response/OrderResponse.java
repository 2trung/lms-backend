package com.example.LMS.dto.response;

import com.example.LMS.constant.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderResponse {
    String id;
    String paymentId;
    PaymentStatus paymentStatus;
    Float amount;
    LocalDateTime createdAt;
}
