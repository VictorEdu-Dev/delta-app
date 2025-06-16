package org.deltacore.delta.domains.tutoring.dto;

import org.deltacore.delta.domains.activity.dto.ActivityDTO;

import java.util.List;

public record SubjectDTO(
        Long id,
        String code,
        String name,
        Boolean isActive,
        List<ActivityDTO> activities,
        TutoringDTO monitoring
) {
}
