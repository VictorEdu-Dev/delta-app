package org.deltacore.delta.domains.tracks.dto;

public record LearningPathDTO(
        String title,
        String description,
        String imageUrl,
        Integer recommendedLevel,
        Float totalScore
) {}
