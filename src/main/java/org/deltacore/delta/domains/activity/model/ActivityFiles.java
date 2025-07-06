package org.deltacore.delta.domains.activity.model;

import jakarta.persistence.*;
import lombok.*;
import org.deltacore.delta.shared.model.GeneralData;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ActivityFiles extends GeneralData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String fileName;

    @Column(nullable = false, length = 100)
    private String fileType;

    @Column(nullable = false, length = 500)
    private String filePath;

    @Column(nullable = false)
    private Long size;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;
}
