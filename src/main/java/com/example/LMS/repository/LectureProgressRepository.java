package com.example.LMS.repository;

import com.example.LMS.entity.Lecture;
import com.example.LMS.entity.LectureProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LectureProgressRepository extends JpaRepository<LectureProgress, String> {
    LectureProgress findByUserIdAndLectureId(String userId, String lectureId);

    @Query("""
        SELECT lp FROM LectureProgress lp
        WHERE lp.user.Id = :userId AND lp.lecture.course.id = :courseId
    """)
    List<LectureProgress> findByUserIdAndCourseId(@Param("userId") String userId, @Param("courseId") String courseId);

    List<LectureProgress> findByUserIdAndLecture_Course_Id(String userId, String courseId);
}
