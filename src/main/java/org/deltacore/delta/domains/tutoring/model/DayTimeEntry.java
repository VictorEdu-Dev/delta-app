package org.deltacore.delta.domains.tutoring.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "monitoring_day_times")
public class DayTimeEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DaysWeek dayOfWeek;

    @ElementCollection
    @CollectionTable(name = "day_times", joinColumns = @JoinColumn(name = "day_time_entry_id"))
    @Column(name = "time")
    private List<LocalDateTime> times;

    @Column(nullable = false)
    private Integer duration;
}
