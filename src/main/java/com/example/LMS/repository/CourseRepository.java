package com.example.LMS.repository;

import com.example.LMS.constant.CourseCategory;
import com.example.LMS.constant.CourseLanguage;
import com.example.LMS.constant.CourseLevel;
import com.example.LMS.entity.Course;
import com.example.LMS.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CourseRepository extends JpaRepository<Course, String> {

    @Query
    Page<Course> findByInstructor(User instructor, Pageable pageable);


    @Query("SELECT c FROM Course c WHERE "
            + "(:category IS NULL OR c.category IN :category) AND "
            + "(:level IS NULL OR c.level IN :level) AND "
            + "(:language IS NULL OR c.language IN :language)")
    List<Course> findByFilters(@Param("category") List<CourseCategory> category,
                               @Param("level") List<CourseLevel> level,
                               @Param("language") List<CourseLanguage> language,
                               Sort sort);
}
