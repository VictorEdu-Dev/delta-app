package org.deltacore.delta.domains.tutoring.model;

import jakarta.persistence.*;
import lombok.*;
import org.deltacore.delta.shared.model.GeneralData;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "monitoring_day_times")
public class DayTimeEntry extends GeneralData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entry_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DaysWeek dayOfWeek;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "day_times",
            joinColumns = @JoinColumn(name = "entry_id")
    )
    @Column(name = "time", nullable = false)
    private List<LocalDateTime> times;

    @Column(nullable = false)
    private Integer duration;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "monitoring_id", nullable = false)
    private Tutoring tutoring;
}
