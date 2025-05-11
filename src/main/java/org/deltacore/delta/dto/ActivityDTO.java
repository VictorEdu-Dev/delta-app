package org.deltacore.delta.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.deltacore.delta.model.ActivityType;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record ActivityDTO(
        Long id,
        @NotBlank(message = "{activity.title.not.blank}")
        String title,

        @NotBlank(message = "{activity.description.not.blank}")
        String description,

        @NotNull(message = "{activity.activity_type.not.null}")
        ActivityType activityType,

        @URL(message = "Image URL must be a valid URL.", regexp = "^(https?|ftp)://.*")
        String imageUrl,

        @Min(value = 1, message = "{activity.recommended_level.min}")
        Integer recommendedLevel,

        @NotNull(message = "{activity.max_score.not.null}")
        @DecimalMin(value = "0.0", inclusive = false, message = "{activity.max_score.min}")
        BigDecimal maxScore) {
}
