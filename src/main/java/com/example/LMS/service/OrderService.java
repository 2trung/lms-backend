package com.example.LMS.service;

import com.example.LMS.constant.PaymentStatus;
import com.example.LMS.dto.response.CreateOrderResponse;
import com.example.LMS.dto.response.OrderResponse;
import com.example.LMS.entity.Course;
import com.example.LMS.entity.Order;
import com.example.LMS.entity.User;
import com.example.LMS.exception.NotFoundException;
import com.example.LMS.mapper.OrderMapper;
import com.example.LMS.repository.CourseRepository;
import com.example.LMS.repository.OrderRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    @Value("${stripe.secret.key}")
    private String secretKey;
    private final CourseRepository courseRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public CreateOrderResponse createPaymentLink(String courseId) {
        User student = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new NotFoundException("Course not found"));
        if (course.getStudents().contains(student)) {
            throw new RuntimeException("You have already enrolled in this course");
        }
        Float price = course.getPrice();
        Stripe.apiKey = secretKey;
        SessionCreateParams params = SessionCreateParams.builder().setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:5173/student/payment-return?paymentId={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:5173/")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount((long) (price * 100))
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName(course.getTitle()).build()).build()).build()).build();

        try {
            Session session = Session.create(params);
            Order order = Order.builder()
                    .student(student)
                    .paymentId(session.getId())
                    .paymentStatus(PaymentStatus.PENDING)
                    .amount(price)
                    .course(course)
                    .build();
            orderRepository.save(order);
            return CreateOrderResponse.builder()
                    .id(order.getId())
                    .paymentId(order.getPaymentId())
                    .paymentStatus(order.getPaymentStatus())
                    .amount(order.getAmount())
                    .createdAt(order.getCreatedAt())
                    .paymentUrl(session.getUrl())
                    .build();
        } catch (Exception e) {
            log.info("Failed to create payment link", e);
            throw new RuntimeException("Failed to create payment link", e);
        }
    }

    public OrderResponse verifyPayment(String paymentId) throws StripeException {
        User student = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Order order = orderRepository.findByPaymentId(paymentId).orElseThrow(() -> new NotFoundException("Order not found"));
//        if (order.getPaymentStatus().equals(PaymentStatus.SUCCESS)) {
//            return orderMapper.toOrderResponse(order);
//        }

        Stripe.apiKey = secretKey;
        Session session = Session.retrieve(paymentId);
        if (session.getPaymentStatus().equals("paid")) {
            order.setPaymentStatus(PaymentStatus.SUCCESS);
            Course course = courseRepository.findById(order.getCourse().getId()).orElseThrow(() -> new NotFoundException("Course not found"));
            if (Objects.equals(student.getId(), order.getStudent().getId())) {
                course.getStudents().add(student);
            }
            courseRepository.save(course);
            return orderMapper.toOrderResponse(orderRepository.save(order));
        }
        return null;
    }
}
