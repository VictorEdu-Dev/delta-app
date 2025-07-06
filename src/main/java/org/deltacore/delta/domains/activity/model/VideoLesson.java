package org.deltacore.delta.domains.activity.model;


import jakarta.persistence.*;
import lombok.*;
import org.deltacore.delta.shared.model.GeneralData;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class VideoLesson extends GeneralData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String videoId;

    @Column(nullable = false, length = 150)
    private String title;

    @Lob
    @Column(length = 500)
    private String description;

    @Column(nullable = false, length = 200)
    private String videoUrl;

    @Column(length = 10)
    private String videoDuration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
    @ToString.Exclude
    private Activity activity;
}
