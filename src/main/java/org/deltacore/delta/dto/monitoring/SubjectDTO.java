package org.deltacore.delta.dto.monitoring;

import org.deltacore.delta.dto.activities.ActivityDTO;

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
