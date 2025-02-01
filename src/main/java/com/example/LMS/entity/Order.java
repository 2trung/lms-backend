package com.example.LMS.entity;

import com.example.LMS.constant.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne()
    User student;

    String paymentId;

    @Enumerated(EnumType.STRING)
    PaymentStatus paymentStatus;

    Float amount;

    @ManyToOne()
    Course course;

    @CreationTimestamp
    LocalDateTime createdAt;
}
