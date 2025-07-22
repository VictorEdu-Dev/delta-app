package org.deltacore.delta.domains.profile.model;

import jakarta.persistence.*;
import lombok.*;
import org.deltacore.delta.domains.tutoring.model.Tutoring;
import org.deltacore.delta.shared.model.GeneralData;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Builder(toBuilder = true)
@Getter
@Setter
@ToString
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

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_id", unique = true)
    @ToString.Exclude
    private Profile profile;
}
