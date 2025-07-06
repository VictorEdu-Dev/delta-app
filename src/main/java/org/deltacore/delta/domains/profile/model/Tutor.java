package org.deltacore.delta.domains.profile.model;

import jakarta.persistence.*;
import lombok.*;
import org.deltacore.delta.domains.tutoring.model.Tutoring;
import org.deltacore.delta.shared.model.GeneralData;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "monitor")
public class Tutor extends GeneralData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime startDate;

    @Column
    private LocalDateTime endDate;

    @Column(nullable = false)
    private boolean isActive;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_monitor_id", nullable = false, unique = true)
    @ToString.Exclude
    private User userMonitor;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "monitor")
    private Tutoring tutoring;
}
