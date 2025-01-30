package com.example.LMS.dto.request;

import com.example.LMS.constant.CourseCategory;
import com.example.LMS.constant.CourseLanguage;
import com.example.LMS.constant.CourseLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EditCourseRequest {
    @NotBlank(message = "Title is required")
    String title;
    @NotBlank(message = "Subtitle is required")
    String subtitle;
    @NotBlank(message = "Description is required")
    String description;
    @NotNull(message = "Category is required")
    CourseCategory category;
    @NotNull(message = "Language is required")
    CourseLanguage language;
    @NotNull(message = "Level is required")
    CourseLevel level;
    @NotNull(message = "Pricing is required")
    @Positive(message = "Pricing must be positive")
    String price;
    @NotBlank(message = "Objectives are required")
    String objectives;
    @NotBlank(message = "Welcome message is required")
    String welcomeMessage;
    @NotBlank(message = "Image is required")
    String image;

    @NotNull(message = "Curriculum is required")
    @Size(min = 1, message = "At least one lecture is required")
    List<LectureRequest> curriculum;

    @Getter
    @Setter
    public static class LectureRequest {
        String id;
        @NotBlank(message = "Curriculum title is required")
        String title;
        @NotBlank(message = "Curriculum public ID is required")
        String videoUrl;
        Boolean freePreview;
    }
}
