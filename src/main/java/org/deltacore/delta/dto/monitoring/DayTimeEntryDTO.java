package org.deltacore.delta.dto.monitoring;

import lombok.Builder;
import org.deltacore.delta.model.monitoring.DaysWeek;

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
