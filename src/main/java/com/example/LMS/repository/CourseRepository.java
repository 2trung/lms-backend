package com.example.LMS.repository;

import com.example.LMS.entity.Course;
import com.example.LMS.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CourseRepository extends JpaRepository<Course, String> {
    Page<Course> findByInstructor(User instructor, Pageable pageable);
}
