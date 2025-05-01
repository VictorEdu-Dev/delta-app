package org.deltacore.delta.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class LearningPath extends GeneralData {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID learningPathId;
    private String title;
    private String description;
    private String imageUrl;
    private Integer recommendedLevel;
    private Float totalScore;

    //private List<String> activities;

}
