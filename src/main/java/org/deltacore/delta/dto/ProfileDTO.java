package org.deltacore.delta.dto;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record ProfileDTO(
        FullNameDTO fullName,
        byte[] profileImage,
        Float totalScore,
        String bio,
        UUID userId
) {
    public record FullNameDTO(String firstName, String lastName) {}
}