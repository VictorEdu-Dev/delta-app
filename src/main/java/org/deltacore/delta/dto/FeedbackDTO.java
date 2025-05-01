package org.deltacore.delta.dto;

import java.time.LocalDateTime;

public record FeedbackDTO(
        String comment,
        Integer rating,
        LocalDateTime submittedAt
) {}
