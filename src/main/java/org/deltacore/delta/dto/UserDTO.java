package org.deltacore.delta.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record UserDTO(
        String username,
        String email,
        String passwordHash,
        LocalDateTime createdAt
) {}

