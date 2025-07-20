package org.deltacore.delta.domains.activity.model;

import jakarta.persistence.*;
import lombok.*;
import org.deltacore.delta.domains.tutoring.model.Subject;
import org.deltacore.delta.shared.model.GeneralData;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
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

    @Column(length = 1000)
    private String imageUrl;

    @Column(name = "difficulty_level", nullable = false)
    private Integer difficultyLevel;

    @Setter(AccessLevel.NONE)
    @Column(precision = 19, scale = 4)
    private BigDecimal maxScore;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @Setter(AccessLevel.NONE)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityStatus status;

    @Setter(AccessLevel.NONE)
    @Column(name = "complete", nullable = false)
    private boolean completed;

    @Setter(AccessLevel.NONE)
    @Column(name = "timestamp")
    private LocalDateTime completionTimestamp;

    @Setter(AccessLevel.NONE)
    @Column(name = "last_access")
    private LocalDateTime lastAccess;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    @ToString.Exclude
    private Subject subject;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "activity", orphanRemoval = true)
    @ToString.Exclude
    private List<ActivityFiles> files;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "activity", orphanRemoval = true)
    @ToString.Exclude
    private List<VideoLesson> videoUrl;

    public void markAsCompleted() {
        this.completed = true;
        this.completionTimestamp = LocalDateTime.now();
        this.status = ActivityStatus.COMPLETED;
    }

    public void setDefaultValues() {
        if (this.deadline == null || this.deadline.isBefore(LocalDateTime.now()))
            this.deadline = LocalDateTime.now().plusDays(7);
        this.status = ActivityStatus.PENDING;
    }

    public void setMaxScore() {
        this.maxScore = switch (this.difficultyLevel) {
            case 1 -> new BigDecimal(20);
            case 2 -> new BigDecimal(25);
            case 3 -> new BigDecimal(30);
            default -> BigDecimal.ZERO;
        };
    }

    public void setLastAccess() {
        this.lastAccess = LocalDateTime.now();
    }

    public void setImageUrl() {
        this.imageUrl = "https://cdn.pixabay.com/photo/2020/09/23/03/54/background-5594879_1280.jpg";
    }
}
