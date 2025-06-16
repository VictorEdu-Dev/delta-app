package org.deltacore.delta.domains.auth.model;

import jakarta.persistence.*;
import lombok.*;
import org.deltacore.delta.domains.profile.model.Profile;
import org.deltacore.delta.shared.model.GeneralData;
import org.deltacore.delta.domains.tutoring.model.Tutoring;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@Entity(name = "user_delta")
@NoArgsConstructor
public class User extends GeneralData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(length = 255, unique = true)
    private String email;

    @Column(nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    private Roles role;

    @Column(nullable = false, length = 70)
    private String passwordHash;

    private LocalDateTime createdAt;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
    private List<Tutoring> tutorings;
}
