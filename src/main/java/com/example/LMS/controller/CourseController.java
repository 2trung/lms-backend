package com.example.LMS.controller;

import com.example.LMS.constant.CourseCategory;
import com.example.LMS.constant.CourseLanguage;
import com.example.LMS.constant.CourseLevel;
import com.example.LMS.dto.SuccessResponse;
import com.example.LMS.dto.request.CreateCourseRequest;
import com.example.LMS.dto.request.EditCourseRequest;
import com.example.LMS.dto.response.InstructorCourseResponse;
import com.example.LMS.dto.response.StudentCourseResponse;
import com.example.LMS.service.CourseService;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/course")
@RestController
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
public class CourseController {
    CourseService courseService;

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/add")
    public SuccessResponse<InstructorCourseResponse> addCourse(@Validated @RequestBody CreateCourseRequest course) {
        return new SuccessResponse<>(200, "Success", courseService.createCourse(course));
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PutMapping("/update/{id}")
    public SuccessResponse<InstructorCourseResponse> updateCourse(@Validated @RequestBody EditCourseRequest course,@NotBlank @PathVariable String id) {
        return new SuccessResponse<>(200, "Success", courseService.updateCourse(id, course));
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/get")
    public SuccessResponse<Page<InstructorCourseResponse>> getCourse(@PageableDefault(size = 10) Pageable pageable) {
        return new SuccessResponse<>(200, "Success", courseService.getCoursesByInstructor(pageable));
    }

    @GetMapping("/detail/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public SuccessResponse<InstructorCourseResponse> getCourse(@PathVariable String id) {
        return new SuccessResponse<>(200, "Success", courseService.getCourse(id));
    }

    @GetMapping("/student/get")
    @PreAuthorize("hasRole('USER')")
    public SuccessResponse<List<StudentCourseResponse>> getStudentCourse(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false) List<CourseCategory> category,
            @RequestParam(required = false) List<CourseLevel> level,
            @RequestParam(required = false) List<CourseLanguage> language,
            @RequestParam(required = false, defaultValue = "title:asc") String sortBy
    ) {
        return new SuccessResponse<>(200, "Success", courseService.getAllCourses(category, level, language, sortBy));
    }

    @GetMapping("/student/detail/{id}")
    @PreAuthorize("hasRole('USER')")
    public SuccessResponse<StudentCourseResponse> getStudentCourse(@PathVariable String id) {
        return new SuccessResponse<>(200, "Success", courseService.getStudentCourse(id));
    }

    @GetMapping("/student/purchase-info/{id}")
    @PreAuthorize("hasRole('USER')")
    public SuccessResponse<Boolean> getStudentCoursePurchaseInfo(@PathVariable String id) {
        return new SuccessResponse<>(200, "Success", courseService.getStudentCoursePurchaseInfo(id));
    }

    @GetMapping("/progress/{id}")
    @PreAuthorize("hasRole('USER')")
    public SuccessResponse<Object> getStudentCourseProgress(@PathVariable String id) {
        return new SuccessResponse<>(200, "Success", courseService.getCurrentCourseProgress(id));
    }

    @PostMapping("/mark-viewed")
    @PreAuthorize("hasRole('USER')")
    public SuccessResponse<?> markLectureAsViewed(@RequestBody Map<String, String> data) {
        String courseId = data.get("courseId");
        String lectureId = data.get("lectureId");
        courseService.markLectureAsViewed(courseId, lectureId);
        return new SuccessResponse<>(200, "Lecture marked as viewed");
    }
    @PostMapping("/reset-progress")
    @PreAuthorize("hasRole('USER')")
    public SuccessResponse<?> resetCourseProgress(@RequestBody Map<String, String> data) {
        String courseId = data.get("courseId");
        courseService.resetCourseProgress(courseId);
        return new SuccessResponse<>(200, "Course progress reset successfully");
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public SuccessResponse<List<StudentCourseResponse>> getMyCourses() {
        return new SuccessResponse<>(200, "Courses retrieved successfully", courseService.getMyCourses());
    }
}
