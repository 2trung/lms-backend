package com.example.LMS.repository;

import com.example.LMS.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, String> {

}
