package com.example.LMS.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.NumberFormat;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateCourseRequest {
    @NotBlank(message = "Title is required")
    String title;
    @NotBlank(message = "Description is required")
    String description;
    @NotBlank(message = "Category is required")
    String category;
    @NotBlank(message = "Language is required")
    String language;
    @NotBlank(message = "Level is required")
    String level;
    @NotBlank(message = "Price is required")
    String price;
    @NotBlank(message = "Objectives are required")
    String objectives;
    @NotBlank(message = "Welcome message is required")
    String welcomeMessage;
    @NotBlank(message = "Image is required")
    String image;
    @NotNull(message = "Pricing is required")
    Float pricing;
    @NotNull(message = "Curriculum is required")
    List<LectureRequest> curriculum;

    static class LectureRequest {
        @NotBlank(message = "Curriculum title is required")
        String title;
        @NotBlank(message = "Curriculum public ID is required")
        String publicId;
        Boolean freePreview;
    }
}
