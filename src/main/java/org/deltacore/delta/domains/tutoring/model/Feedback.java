package org.deltacore.delta.domains.tutoring.model;

import jakarta.persistence.*;
import lombok.*;
import org.deltacore.delta.domains.profile.model.Profile;
import org.deltacore.delta.shared.model.GeneralData;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Feedback extends GeneralData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;
    private Integer rating;
    private LocalDateTime submittedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    @ToString.Exclude
    private Profile profile;
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Tutoring tutoring;
}
