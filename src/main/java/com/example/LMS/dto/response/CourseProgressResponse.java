package com.example.LMS.dto.response;

import com.example.LMS.entity.LectureProgress;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class CourseProgressResponse {
    StudentCourseResponse courseDetails;
    List<LectureProgress> progress;
    Boolean isPurchased;
    Boolean completed;
}
