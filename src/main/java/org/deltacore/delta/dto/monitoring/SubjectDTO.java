package org.deltacore.delta.dto.monitoring;

public record SubjectDTO(
        Long id,
        String code,
        String name,
        Boolean isActive
) {
}
