package org.deltacore.delta.model.user;

import jakarta.persistence.*;
import lombok.*;
import org.deltacore.delta.model.GeneralData;
import org.deltacore.delta.model.Subject;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Monitor extends GeneralData {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LocalDateTime startDate;
    private LocalDateTime endDate;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_monitor_id")
    private User userMonitor;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Subject> subjects;
}
