package org.deltacore.delta.domains.tutoring.dto;

import lombok.Builder;
import org.deltacore.delta.domains.tutoring.model.DaysWeek;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record DayTimeEntryDTO(
        Long id,
        DaysWeek dayOfWeek,
        List<LocalDateTime> times,
        Integer duration
) {
}
