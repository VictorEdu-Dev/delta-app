package org.deltacore.delta.domains.tutoring.model;

import jakarta.persistence.*;
import lombok.*;
import org.deltacore.delta.shared.model.GeneralData;
import org.deltacore.delta.domains.auth.model.Tutor;
import org.deltacore.delta.domains.auth.model.User;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
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
    @JoinColumn(name = "monitor_id")
    private Tutor tutor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "monitoring_users",
            joinColumns = @JoinColumn(name = "monitoring_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "monitoring_id")
    private List<DayTimeEntry> daysOfWeek;
}
