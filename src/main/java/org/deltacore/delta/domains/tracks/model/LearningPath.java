package org.deltacore.delta.domains.tracks.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.deltacore.delta.domains.activity.model.Activity;
import org.deltacore.delta.shared.model.GeneralData;

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

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Activity> activities;

}
