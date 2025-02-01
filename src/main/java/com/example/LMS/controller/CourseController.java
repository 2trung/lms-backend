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

    @GetMapping("/student/get")
    public SuccessResponse<List<StudentCourseResponse>> getStudentCourse(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false) List<CourseCategory> category,
            @RequestParam(required = false) List<CourseLevel> level,
            @RequestParam(required = false) List<CourseLanguage> language,
            @RequestParam(required = false, defaultValue = "title:asc") String sortBy
    ) {
        return new SuccessResponse<>(200, "Courses retrieved successfully", courseService.getAllCourses(category, level, language, sortBy));
    }

    @GetMapping("/student/detail/{id}")
    public SuccessResponse<StudentCourseResponse> getStudentCourse(@PathVariable String id) {
        return new SuccessResponse<>(200, "Course retrieved successfully", courseService.getStudentCourse(id));
    }

    @GetMapping("/student/purchase-info/{id}")
    public SuccessResponse<Boolean> getStudentCoursePurchaseInfo(@PathVariable String id) {
        return new SuccessResponse<>(200, "Success", courseService.getStudentCoursePurchaseInfo(id));
    }

    @GetMapping("/progress/{id}")
    public SuccessResponse<Object> getStudentCourseProgress(@PathVariable String id) {
        return new SuccessResponse<>(200, "Courses retrieved successfully", courseService.getCurrentCourseProgress(id));
    }

    @PostMapping("/mark-viewed")
    public SuccessResponse<?> markLectureAsViewed(@RequestBody Map<String, String> data) {
        String courseId = data.get("courseId");
        String lectureId = data.get("lectureId");
        courseService.markLectureAsViewed(courseId, lectureId);
        return new SuccessResponse<>(200, "Lecture marked as viewed");
    }
    @PostMapping("/reset-progress")
    public SuccessResponse<?> resetCourseProgress(@RequestBody Map<String, String> data) {
        String courseId = data.get("courseId");
        courseService.resetCourseProgress(courseId);
        return new SuccessResponse<>(200, "Course progress reset successfully");
    }

    @GetMapping("/my")
    public SuccessResponse<List<StudentCourseResponse>> getMyCourses() {
        return new SuccessResponse<>(200, "Courses retrieved successfully", courseService.getMyCourses());
    }
}
