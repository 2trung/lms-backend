package com.example.LMS.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LectureProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @JsonIgnore
    @ManyToOne
    User user;

    @ManyToOne
    Lecture lecture;

    Boolean viewed;

    LocalDateTime dateViewed;
}
