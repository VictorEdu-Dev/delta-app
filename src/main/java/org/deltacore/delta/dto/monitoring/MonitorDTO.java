package org.deltacore.delta.dto.monitoring;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MonitorDTO(
        Long id,
        LocalDate startDate,
        LocalDate endDate,
        Long userMonitorId
) {}
