package com.example.LMS.service;

import com.example.LMS.entity.Course;
import com.example.LMS.entity.User;
import com.example.LMS.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StripeSevice {

    @Value("${stripe.secret.key}")
    private String secretKey;
    private final  CourseRepository courseRepository;

    public void createPaymentLink(String courseId) {
        User student = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            throw new RuntimeException("Course not found");
        }
        // Create payment link

    }
}
