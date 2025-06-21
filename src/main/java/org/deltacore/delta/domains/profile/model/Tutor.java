package org.deltacore.delta.domains.profile.model;

import jakarta.persistence.*;
import lombok.*;
import org.deltacore.delta.shared.model.GeneralData;
import org.deltacore.delta.domains.tutoring.model.Tutoring;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Tutor extends GeneralData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime startDate;

    @Column
    private LocalDateTime endDate;

    @Column(nullable = false)
    private boolean active = true;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_monitor_id", nullable = false)
    private User userMonitor;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "monitor")
    private Tutoring tutoring;

}
