package org.deltacore.delta.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Builder
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class VideoLesson extends GeneralData {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "video_lesson_seq")
    @SequenceGenerator(
            name = "video_lesson_seq",
            sequenceName = "video_lesson_sequence"
    )
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, length = 200)
    private String videoUrl;

    @Column()
    private Integer videoDuration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
    private Activity activity;
}
