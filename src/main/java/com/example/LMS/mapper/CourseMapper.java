package com.example.LMS.mapper;

import com.example.LMS.dto.request.CreateCourseRequest;
import com.example.LMS.dto.request.EditCourseRequest;
import com.example.LMS.dto.response.InstructorCourseResponse;
import com.example.LMS.entity.Course;
import com.example.LMS.entity.Lecture;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    @Mapping(target = "curriculum", source = "curriculum")
    Course toCourse(CreateCourseRequest request);

    @Mapping(target = "id", ignore = true)
    Lecture toLecture(CreateCourseRequest.LectureRequest request);

    InstructorCourseResponse toInstructorCourseResponse(Course course);

    @Mapping(target = "curriculum", source = "curriculum")
    Course toCourse(EditCourseRequest request);

    @Mapping(target = "id", source = "id")
    Lecture toLecture(EditCourseRequest.LectureRequest request);

    default Page<InstructorCourseResponse> toInstructorCourseResponse(Page<Course> courses) {
        return new PageImpl<>(
                courses.stream()
                        .map(this::toInstructorCourseResponse)
                        .collect(Collectors.toList()),
                courses.getPageable(),
                courses.getTotalElements()
        );
    }
}
