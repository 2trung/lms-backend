package com.example.LMS.repository;

import com.example.LMS.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {
    Optional<Order> findByPaymentId(String paymentId);
}
