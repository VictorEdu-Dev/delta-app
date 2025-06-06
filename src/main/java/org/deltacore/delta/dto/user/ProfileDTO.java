package org.deltacore.delta.dto.user;


import jakarta.annotation.Nullable;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProfileDTO(
        @Size(max = 100, min = 1, message = "{profile.name.size}")
        String name,

        @Nullable
        byte[] profileImage,

        @Pattern(regexp = "\\+?\\d{10,15}", message = "{profile.phone.pattern}")
        String phoneNumber,

        @DecimalMin(value = "0.0", message = "{profile.total_score.min}")
        BigDecimal totalScore,

        @Min(value = 1, message = "{profile.level.min}")
        Integer level,

        @Size(max = 150, min = 1, message = "{profile.bio.size}")
        String bio,

        Long userId
) {}
