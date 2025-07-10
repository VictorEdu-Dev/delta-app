package org.deltacore.delta.domains.profile.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.math.BigDecimal;

@Builder(toBuilder = true)
public record ProfileDTO(
        @Size(max = 100, min = 1, message = "{profile.name.size}")
        String name,

        String profileImage,

        @Pattern(regexp = "\\d{10,15}", message = "{profile.phone.pattern}")
        String phoneNumber,

        BigDecimal totalScore,

        Integer level,

        @Size(max = 150, message = "{profile.bio.size}")
        String bio
) {}
