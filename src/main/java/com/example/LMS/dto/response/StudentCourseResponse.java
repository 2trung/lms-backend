package com.example.LMS.dto.response;

import com.example.LMS.constant.CourseCategory;
import com.example.LMS.constant.CourseLanguage;
import com.example.LMS.constant.CourseLevel;
import com.example.LMS.entity.Lecture;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentCourseResponse {
    String id;
    InstructorResponse instructor;
    String title;
    String subtitle;
    String description;
    String image;
    CourseLevel level;
    CourseCategory category;
    CourseLanguage language;
    String welcomeMessage;
    Float price;
    String objectives;
    List<Lecture> curriculum;
    LocalDateTime createdAt;
    int students;
}
