package com.example.LMS.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String title;

    String videoUrl;

    Boolean freePreview;

    @JsonIgnore
    @ManyToOne()
    Course course;
}
