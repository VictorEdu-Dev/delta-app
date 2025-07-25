package org.deltacore.delta.domains.tutoring.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.deltacore.delta.domains.activity.dto.ActivityDTO;

import java.util.List;

public record SubjectDTO(
        Long id,
        String code,
        String name,
        Boolean isActive,
        @Schema(hidden = true)
        List<ActivityDTO> activities,
        @Schema(hidden = true)
        TutoringDTO monitoring
) {
        public record SubjectInfoDTO (
                Long id,
                String code,
                String name,
                Boolean isActive
        ) {}
}
