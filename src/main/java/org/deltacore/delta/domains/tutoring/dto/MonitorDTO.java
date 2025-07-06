package org.deltacore.delta.domains.tutoring.dto;

import lombok.Builder;
import org.deltacore.delta.domains.profile.dto.UserDTO;

import java.time.LocalDate;

@Builder(toBuilder = true)
public record MonitorDTO(
        Long id,
        LocalDate startDate,
        LocalDate endDate,
        UserDTO userMonitor,
        TutoringDTO monitoring
) {}
