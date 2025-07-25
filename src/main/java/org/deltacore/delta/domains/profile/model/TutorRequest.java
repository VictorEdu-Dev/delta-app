package org.deltacore.delta.domains.profile.model;

import jakarta.persistence.*;
import lombok.*;
import org.deltacore.delta.domains.tutoring.model.Subject;
import org.deltacore.delta.shared.model.GeneralData;

import java.time.OffsetDateTime;

@Builder
@Setter
@Getter
@Entity(name = "tutor_request")
@NoArgsConstructor
@AllArgsConstructor
public class TutorRequest extends GeneralData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(name = "file_path", nullable = false, columnDefinition = "TEXT")
    private String filePath;

    @Column
    private Boolean approved;

    @Setter(value = AccessLevel.NONE)
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "observation", columnDefinition = "TEXT", length = 500)
    private String observation;
}
