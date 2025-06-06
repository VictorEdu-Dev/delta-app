package org.deltacore.delta.model.monitoring;

import jakarta.persistence.*;
import lombok.*;
import org.deltacore.delta.model.GeneralData;
import org.deltacore.delta.model.user.Monitor;
import org.deltacore.delta.model.user.User;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Monitoring extends GeneralData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monitor_id")
    private Monitor monitor;

    @Column(length = 150)
    private String description;

    @Column(length = 300)
    private String urlThumbnail;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "monitoring_id")
    private List<DayTimeEntry> daysOfWeek;

    @Column(length = 50)
    private String location;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Column(nullable = false)
    private Integer vacancies;

    @Enumerated(EnumType.STRING)
    private Modality mode;

    @Column(nullable = false)
    private Boolean isActive;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "monitoring_users",
            joinColumns = @JoinColumn(name = "monitoring_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;
}
