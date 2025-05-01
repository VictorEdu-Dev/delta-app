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
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String title;
    private String description;
    private String videoUrl;
    private Integer videoDuration;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
    private Activity activity;
}
