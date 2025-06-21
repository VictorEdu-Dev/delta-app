package org.deltacore.delta.domains.auth.model;

import jakarta.persistence.*;
import lombok.*;
import org.deltacore.delta.domains.profile.model.User;
import org.deltacore.delta.shared.model.GeneralData;

import java.time.Instant;
import java.util.UUID;

@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class RefreshToken extends GeneralData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private UUID token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private boolean revoked = false;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();
}
