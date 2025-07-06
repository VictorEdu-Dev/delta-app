package org.deltacore.delta.domains.tutoring.model;

import jakarta.persistence.*;
import lombok.*;
import org.deltacore.delta.domains.profile.model.Tutor;
import org.deltacore.delta.domains.profile.model.User;
import org.deltacore.delta.shared.model.GeneralData;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "monitoring")
public class Tutoring extends GeneralData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150)
    private String description;

    @Column(length = 1000)
    private String urlThumbnail;

    @Column(length = 50)
    private String location;

    @Column(nullable = false)
    private Integer vacancies;

    @Enumerated(EnumType.STRING)
    private Modality mode;

    @Column(nullable = false)
    private Boolean isActive;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monitor_id", unique = true)
    @ToString.Exclude
    private Tutor monitor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", unique = true)
    @ToString.Exclude
    private Subject subject;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "monitoring_users",
            joinColumns = @JoinColumn(name = "monitoring_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @ToString.Exclude
    private List<User> users;

    @OneToMany(mappedBy = "tutoring", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ToString.Exclude
    private List<DayTimeEntry> daysOfWeek;
}
