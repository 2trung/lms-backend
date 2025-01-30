package com.example.LMS.controller;

import com.example.LMS.dto.SuccessResponse;
import com.example.LMS.dto.request.CreateCourseRequest;
import com.example.LMS.dto.request.EditCourseRequest;
import com.example.LMS.dto.response.InstructorCourseResponse;
import com.example.LMS.entity.Course;
import com.example.LMS.service.CourseService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/course")
@RestController
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
public class CourseController {
    CourseService courseService;

    @PostMapping("/add")
    public SuccessResponse<InstructorCourseResponse> addCourse(@Validated @RequestBody CreateCourseRequest course) {
        return new SuccessResponse<>(200, "Course added successfully", courseService.createCourse(course));
    }

    @PutMapping("/update/{id}")
    public SuccessResponse<InstructorCourseResponse> updateCourse(@Validated @RequestBody EditCourseRequest course,@NotBlank @PathVariable String id) {
        return new SuccessResponse<>(200, "Course updated successfully", courseService.updateCourse(id, course));
    }

    @GetMapping("/get")
    public SuccessResponse<Page<InstructorCourseResponse>> getCourse(@PageableDefault(size = 10) Pageable pageable) {
        return new SuccessResponse<>(200, "Courses retrieved successfully", courseService.getCoursesByInstructor(pageable));
    }

    @GetMapping("/detail/{id}")
    public SuccessResponse<InstructorCourseResponse> getCourse(@PathVariable String id) {
        return new SuccessResponse<>(200, "Course retrieved successfully", courseService.getCourse(id));
    }
}
