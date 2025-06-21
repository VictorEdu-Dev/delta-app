package org.deltacore.delta.domains.profile.dto;

import jakarta.validation.constraints.*;
import org.deltacore.delta.domains.tutoring.dto.TutoringDTO;

import java.time.LocalDateTime;

public record TutorDTO(
        Long id,

        @NotNull(message = "{tutor.start_date.not.null}")
        LocalDateTime startDate,

        LocalDateTime endDate,

        boolean active,

        @NotNull(message = "{tutor.user.not.null}")
        UserBasicDTO userMonitor,

        @NotNull(message = "user.tutorings.not.empty")
        TutoringDTO tutoring
) {}

