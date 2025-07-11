package org.deltacore.delta.domains.activity.dto;

import org.deltacore.delta.domains.activity.model.ActivityStatus;
import org.deltacore.delta.domains.activity.model.ActivityType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ActivityMiniatureDTO(
        Long id,
        String title,
        ActivityStatus status,
        ActivityType activityType,
        BigDecimal maxScore,
        Integer recommendedLevel,
        LocalDateTime deadline) {}
