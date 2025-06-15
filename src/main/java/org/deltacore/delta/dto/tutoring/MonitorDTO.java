package org.deltacore.delta.dto.tutoring;

import lombok.Builder;
import org.deltacore.delta.dto.user.UserDTO;

import java.time.LocalDate;

@Builder
public record MonitorDTO(
        Long id,
        LocalDate startDate,
        LocalDate endDate,
        UserDTO userMonitor,
        TutoringDTO monitoring
) {}
