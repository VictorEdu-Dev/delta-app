package org.deltacore.delta.domains.tutoring.dto;

import java.time.LocalDateTime;

public record FeedbackDTO(
        String comment,
        Integer rating,
        LocalDateTime submittedAt
) {}
