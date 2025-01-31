package com.example.LMS.service;

import com.example.LMS.constant.CourseCategory;
import com.example.LMS.constant.CourseLanguage;
import com.example.LMS.constant.CourseLevel;
import com.example.LMS.dto.request.CreateCourseRequest;
import com.example.LMS.dto.request.EditCourseRequest;
import com.example.LMS.dto.response.InstructorCourseResponse;
import com.example.LMS.dto.response.StudentCourseResponse;
import com.example.LMS.entity.Course;
import com.example.LMS.entity.User;
import com.example.LMS.mapper.CourseMapper;
import com.example.LMS.repository.CourseRepository;
import com.example.LMS.utils.CustomPageable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class CourseService implements ICourseService {
    CourseRepository courseRepository;
    CourseMapper courseMapper;

    public InstructorCourseResponse createCourse(CreateCourseRequest request) {
        User instructor = getUserDetails();
        Course course = courseMapper.toCourse(request);
        course.setInstructor(instructor);
        course.setIsPublished(true);
        course.getCurriculum().forEach(lecture -> lecture.setCourse(course));
        return courseMapper.toInstructorCourseResponse(courseRepository.save(course));
    }

    public InstructorCourseResponse updateCourse(String id, EditCourseRequest request) {
        User instructor = getUserDetails();
        Course courseToUpdate = courseRepository.findById(id).orElse(null);
        if (courseToUpdate == null || !Objects.equals(courseToUpdate.getInstructor().getId(), instructor.getId())) {
            throw new RuntimeException("Course not found");
        }
        courseToUpdate.setTitle(request.getTitle());
        courseToUpdate.setSubtitle(request.getSubtitle());
        courseToUpdate.setDescription(request.getDescription());
        courseToUpdate.setCategory(request.getCategory());
        courseToUpdate.setLanguage(request.getLanguage());
        courseToUpdate.setLevel(request.getLevel());
        courseToUpdate.setPrice(Float.parseFloat(request.getPrice()));
        courseToUpdate.setObjectives(request.getObjectives());
        courseToUpdate.setWelcomeMessage(request.getWelcomeMessage());
        courseToUpdate.setImage(request.getImage());
        courseToUpdate.getCurriculum().clear();
        request.getCurriculum().forEach(lectureRequest -> {
            var lecture = courseMapper.toLecture(lectureRequest);
            courseToUpdate.getCurriculum().add(lecture);
        });
        courseToUpdate.getCurriculum().forEach(lecture -> lecture.setCourse(courseToUpdate));
        var updatedCourse = courseRepository.save(courseToUpdate);
        return courseMapper.toInstructorCourseResponse(updatedCourse);
    }

    public Page<InstructorCourseResponse> getCoursesByInstructor(Pageable pageable) {
        CustomPageable customPageable = new CustomPageable(pageable);
        User instructor = getUserDetails();
        Page<Course> courses = courseRepository.findByInstructor(instructor, customPageable);
        return courseMapper.toInstructorCourseResponse(courses);
    }

    public InstructorCourseResponse getCourse(String id) {
        User instructor = getUserDetails();
        Course course = courseRepository.findById(id).orElse(null);
        if (course == null || !Objects.equals(course.getInstructor().getId(), instructor.getId())) {
            throw new RuntimeException("Course not found");
        }
        return courseMapper.toInstructorCourseResponse(course);
    }

    public List<StudentCourseResponse> getAllCourses(List<CourseCategory> category, List<CourseLevel> level, List<CourseLanguage> language, String sortBy) {
        Sort sort = getSortOrder(sortBy);
        List<Course> courses = new ArrayList<>();
        try {
            courses = courseRepository.findByFilters(
                    (category == null || category.isEmpty()) ? null : category,
                    (level == null || level.isEmpty()) ? null : level,
                    (language == null || language.isEmpty()) ? null : language,
                    sort
            );
        } catch (Exception e) {
            log.error("Error while fetching courses", e);
        }

        var results = courseMapper.toStudentCourseResponse(courses);
        results.forEach(result -> {
            result.getCurriculum().forEach(lecture -> {
                if (!lecture.getFreePreview()) lecture.setVideoUrl(null);
            });
        });
        return courseMapper.toStudentCourseResponse(courses);
    }

    public StudentCourseResponse getStudentCourse(String id) {
        Course course = courseRepository.findById(id).orElse(null);
        if (course == null) {
            throw new RuntimeException("Course not found");
        }
        var result = courseMapper.toStudentCourseResponse(course);
        result.getCurriculum().forEach(lecture -> {
            if (!lecture.getFreePreview()) lecture.setVideoUrl(null);
        });
        return result;
    }

    public StudentCourseResponse getStudentCoursePurchaseInfo(String id) {
        Course course = courseRepository.findById(id).orElse(null);
        if (course == null) {
            throw new RuntimeException("Course not found");
        }
        User student = getUserDetails();
        if (course.getStudents().contains(student)) {
            var result = courseMapper.toStudentCourseResponse(course);
            result.setIsPurchased(true);
            return result;
        }
        var result = courseMapper.toStudentCourseResponse(course);
        result.getCurriculum().forEach(lecture -> {
            if (!lecture.getFreePreview()) lecture.setVideoUrl(null);
        });
        result.setIsPurchased(false);
        return result;
    }

    private Sort getSortOrder(String sortBy) {
        if (sortBy != null) {
            String[] parts = sortBy.split(":");
            if (parts.length == 2) {
                String field = parts[0]; // "title" hoặc "price"
                if (!field.equals("title") && !field.equals("price")) {
                    throw new RuntimeException("Invalid sort field");
                }
                Sort.Direction direction = parts[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
                return Sort.by(direction, field);
            }
        }
        return Sort.by(Sort.Direction.ASC, "title");
    }


    private User getUserDetails() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
