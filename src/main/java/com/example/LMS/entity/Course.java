package com.example.LMS.entity;

import com.example.LMS.constant.CourseCategory;
import com.example.LMS.constant.CourseLanguage;
import com.example.LMS.constant.CourseLevel;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @ManyToOne
    User instructor;

    String title;

    String subtitle;

    @Column(columnDefinition = "TEXT")
    String description;

    String image;

    @Enumerated(EnumType.STRING)
    CourseLevel level;

    @Enumerated(EnumType.STRING)
    CourseCategory category;

    @Enumerated(EnumType.STRING)
    CourseLanguage language;

    String welcomeMessage;

    Float price;

    @Column(columnDefinition = "TEXT")
    String objectives;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
    List<Lecture> curriculum;

    @ManyToMany
    List<User> students;

    Boolean isPublished;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    LocalDateTime updatedAt;
}
