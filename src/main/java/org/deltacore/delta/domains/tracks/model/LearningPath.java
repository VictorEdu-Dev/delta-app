package org.deltacore.delta.domains.tracks.model;

import jakarta.persistence.*;
import lombok.*;
import org.deltacore.delta.domains.activity.model.Activity;
import org.deltacore.delta.shared.model.GeneralData;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class LearningPath extends GeneralData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private Integer recommendedLevel;
    private Float totalScore;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Activity> activities;
}
