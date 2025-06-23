package org.deltacore.delta.domains.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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

        @Schema(hidden = true)
        UserBasicDTO userMonitor,

        TutoringDTO tutoring
) {}

