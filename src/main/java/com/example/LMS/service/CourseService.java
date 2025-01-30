package com.example.LMS.service;

import com.example.LMS.dto.request.CreateCourseRequest;
import com.example.LMS.dto.request.EditCourseRequest;
import com.example.LMS.dto.response.InstructorCourseResponse;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
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

    private User getUserDetails() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
