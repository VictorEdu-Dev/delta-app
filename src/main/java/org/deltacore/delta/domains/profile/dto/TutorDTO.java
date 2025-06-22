package org.deltacore.delta.domains.profile.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import org.deltacore.delta.domains.tutoring.dto.TutoringDTO;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record TutorDTO(
        Long id,

        @NotNull(message = "{tutor.start_date.not.null}")
        LocalDateTime startDate,

        LocalDateTime endDate,

        boolean isActive,

        UserBasicDTO userMonitor,

        TutoringDTO tutoring
) {}

