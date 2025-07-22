package org.deltacore.delta.domains.profile.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.math.BigDecimal;

@Builder(toBuilder = true)
public record ProfileDTO(
        @Size(max = 100, min = 1, message = "{profile.name.size}")
        String name,

        String profileImage,

        @Pattern(
                regexp = "^\\d{11}$",
                message = "{profile.phone.pattern}"
        )
        String phoneNumber,

        BigDecimal totalScore,

        Integer level,

        @Size(max = 150, message = "{profile.bio.size}")
        String bio
) {
        @Builder(toBuilder = true)
        public record ProfileSimpleDTO(
                String name,
                String profileImage) {
        }

        @Builder(toBuilder = true)
        public record ProfileUpdateDTO(
                UserInfoDTO userInfo,

                @Size(max = 100, min = 1, message = "{profile.name.size}")
                String name,

                @Pattern(
                        regexp = "^\\d{11}$",
                        message = "{profile.phone.pattern}"
                )
                String phoneNumber,

                @Size(max = 150, message = "{profile.bio.size}")
                String bio
        ) {
                public record UserInfoDTO(
                        @NotBlank(message = "{user.username.notblank}")
                        @Size(min = 3, max = 50, message = "{user.username.size}")
                        @Pattern(
                                regexp = "^[a-zA-Z][a-zA-Z0-9._-]*$",
                                message = "{user.username.pattern}"
                        )
                        String username,

                        @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                                message = "{profile.email.pattern}")
                        String email
                ) {}
        }
}
