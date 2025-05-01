package org.deltacore.delta.dto;

public record LearningPathDTO(
        String title,
        String description,
        String imageUrl,
        Integer recommendedLevel,
        Float totalScore
) {}
