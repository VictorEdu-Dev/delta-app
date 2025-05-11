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
        @NotBlank(message = "Title cannot be blank.")
        String title,

        @NotBlank(message = "Description cannot be blank.")
        String description,

        @NotNull(message = "Activity type cannot be null.")
        ActivityType activityType,

        @NotBlank(message = "Image URL cannot be blank.")
        @URL(message = "Image URL must be a valid URL.")
        String imageUrl,

        @Min(value = 1, message = "Recommended level must be at least 1.")
        Integer recommendedLevel,

        @NotNull(message = "Max score cannot be null.")
        @DecimalMin(value = "0.0", inclusive = false, message = "Max score must be greater than 0.")
        BigDecimal maxScore) {
}
