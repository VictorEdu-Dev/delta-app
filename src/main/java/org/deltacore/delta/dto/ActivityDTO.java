package org.deltacore.delta.dto;

import lombok.Builder;
import org.deltacore.delta.model.ActivityType;
import org.deltacore.delta.model.Subject;
import org.deltacore.delta.model.VideoLesson;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
public record ActivityDTO(
        UUID id,
        @NonNull String title,
        String description,
        @NonNull ActivityType activityType,
        String imageUrl,
        Integer recommendedLevel,
        BigDecimal maxScore) {
}
