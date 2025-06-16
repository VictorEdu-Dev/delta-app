package org.deltacore.delta.domains.auth.dto;

import org.deltacore.delta.domains.profile.dto.UserDTO;

import java.time.Instant;

public record BlacklistDTO(
        String token,
        Instant expiryDate,
        UserDTO user
) {}
