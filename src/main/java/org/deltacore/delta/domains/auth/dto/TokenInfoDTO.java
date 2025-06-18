package org.deltacore.delta.domains.auth.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record TokenInfoDTO(
        String meta,
        TokenInfoValueDTO tokenInfoValue
) {
    @Builder
    public record TokenInfoValueDTO(
            String username,
            String token,
            Instant expiresAt
    ) {}
}
