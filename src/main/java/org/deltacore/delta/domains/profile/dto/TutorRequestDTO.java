package org.deltacore.delta.domains.profile.dto;

import lombok.Builder;

import java.time.OffsetDateTime;

@Builder(toBuilder = true)
public record TutorRequestDTO(
        Long id,
        String user,
        String subject,
        OffsetDateTime createdAt,
        String observation
) {
}
