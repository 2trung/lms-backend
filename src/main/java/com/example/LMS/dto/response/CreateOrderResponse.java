package com.example.LMS.dto.response;

import com.example.LMS.constant.PaymentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class CreateOrderResponse {
    String id;
    String paymentId;
    PaymentStatus paymentStatus;
    Float amount;
    LocalDateTime createdAt;
    String paymentUrl;
}
