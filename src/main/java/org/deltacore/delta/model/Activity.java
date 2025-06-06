package org.deltacore.delta.model;

import jakarta.persistence.*;
import lombok.*;
import org.deltacore.delta.model.monitoring.Subject;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Activity extends GeneralData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String title;

    @Column(length = 500, nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityType activityType;

    @Column(length = 150)
    private String imageUrl;

    @Column(nullable = false)
    private Integer recommendedLevel;

    @Column(precision = 19, scale = 4)
    private BigDecimal maxScore;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityStatus status;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "activity")
    private List<VideoLesson> videoUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Column(name = "complete", nullable = false)
    private boolean completed;

    @Column(name = "timestamp")
    private LocalDateTime completionTimestamp;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "activity")
    private List<ActivityFiles> files;

}
