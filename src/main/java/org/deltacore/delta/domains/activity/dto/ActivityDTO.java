package org.deltacore.delta.domains.activity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import org.deltacore.delta.domains.activity.model.ActivityStatus;
import org.deltacore.delta.domains.activity.model.ActivityType;
import org.deltacore.delta.shared.dto.OnCreate;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
        Integer difficultyLevel,

        @NotNull(message = "{activity.max_score.not.null}")
        @DecimalMin(value = "0.0", inclusive = false, message = "{activity.max_score.min}")
        BigDecimal maxScore,

        ActivityStatus status,

        @FutureOrPresent(message = "{activity.deadline.future_or_present}", groups = OnCreate.class)
        LocalDateTime deadline,

        boolean completed,
        LocalDateTime completionTimestamp,

        List<ActivityFilesDTO> files,

        List<ActivityRegister.LinkActivityDTO> links
) {
        @Builder(toBuilder = true)
        public record ActivityRegister(
                @Schema(hidden = true)
                Long id,

                @NotBlank(message = "{activity.title.not.blank}")
                String title,

                @NotBlank(message = "{activity.description.not.blank}")
                String description,

                @NotNull(message = "{activity.activity_type.not.null}")
                ActivityType activityType,

                @Range(min = 1, max = 3, message = "{activity.difficulty_level.range}")
                Integer difficultyLevel,

                @FutureOrPresent(message = "{activity.deadline.future_or_present}", groups = OnCreate.class)
                LocalDate deadline,

                @NotBlank(message = "{activity.subject.not.blank}")
                String subjectCode,

                List<LinkActivityDTO> links
        ) {
                public record LinkActivityDTO(
                        @URL(message = "Link must be a valid URL.", regexp = "^(https?|ftp)://.*")
                        String link,

                        @Size(max = 500, message = "{activity.link.description.size}")
                        String description
                ) {}
        }
}
