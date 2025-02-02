package com.example.LMS.controller;

import com.example.LMS.dto.SuccessResponse;
import com.example.LMS.dto.response.CreateOrderResponse;
import com.example.LMS.dto.response.OrderResponse;
import com.example.LMS.entity.Order;
import com.example.LMS.service.OrderService;
import com.stripe.exception.StripeException;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/order")
@RestController
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@PreAuthorize("hasRole('USER')")
public class OrderController {
    OrderService orderService;

    @PostMapping("/create")
    public SuccessResponse<CreateOrderResponse> createPaymentLink(@RequestBody Map<String, String> data) {
        String courseId = data.get("courseId");
        if (courseId == null) {
            return new SuccessResponse<>(400, "Course id is required", null);
        }
        return new SuccessResponse<>(200, "Payment link created successfully", orderService.createPaymentLink(courseId));
    }

    @PostMapping("/verify")
    public SuccessResponse<OrderResponse> verifyPayment(@RequestBody Map<String, String> data) throws StripeException {
        String paymentId = data.get("paymentId");
        if (paymentId == null) {
            return new SuccessResponse<>(400, "Payment id is required", null);
        }
        return new SuccessResponse<>(200, "Payment verified successfully", orderService.verifyPayment(paymentId));
    }
}
