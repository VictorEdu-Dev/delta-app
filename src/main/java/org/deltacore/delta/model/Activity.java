package org.deltacore.delta.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Activity extends GeneralData {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String title;
    private String description;
    private ActivityType activityType;
    private String imageUrl;
    private Integer recommendedLevel;
    @Column(precision = 19, scale = 4)
    private BigDecimal maxScore;
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<VideoLesson> videoUrl;
    @OneToOne(fetch = FetchType.LAZY)
    private Subject subject;
}
