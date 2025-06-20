package org.deltacore.delta.domains.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record TokenInfoDTO(
        String meta,
        RefreshTokenDTO refreshTokenDTO,
        TokenInfoValueDTO tokenInfoValue
) {
    @Builder
    public record TokenInfoValueDTO(
            String username,
            String token,
            Instant expiresAt
    ) {}

    @Builder
    public record RefreshTokenDTO(
            Long id,

            @NotNull(message = "{auth.refresh_token.not_null}")
            UUID token,

            UserBasicDTO userbasicDTO,
            boolean revoked,
            Instant expiresAt,
            Instant createdAt
    ) {}
}
