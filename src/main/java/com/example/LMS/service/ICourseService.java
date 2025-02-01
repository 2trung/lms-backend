package com.example.LMS.service;

import com.example.LMS.constant.CourseCategory;
import com.example.LMS.constant.CourseLanguage;
import com.example.LMS.constant.CourseLevel;
import com.example.LMS.dto.request.CreateCourseRequest;
import com.example.LMS.dto.request.EditCourseRequest;
import com.example.LMS.dto.response.InstructorCourseResponse;
import com.example.LMS.dto.response.StudentCourseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICourseService {
    InstructorCourseResponse createCourse(CreateCourseRequest request);

    InstructorCourseResponse updateCourse(String id, EditCourseRequest request);

    Page<InstructorCourseResponse> getCoursesByInstructor(Pageable pageable);

    InstructorCourseResponse getCourse(String id);

    List<StudentCourseResponse> getAllCourses(List<CourseCategory> category, List<CourseLevel> level, List<CourseLanguage> language, String sortBy);

    StudentCourseResponse getStudentCourse(String id);

    Boolean getStudentCoursePurchaseInfo(String id);
}
