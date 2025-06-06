package org.deltacore.delta.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
public record UserDTO(
        String username,
        String email,
        String passwordHash,
        LocalDateTime createdAt
) {}

