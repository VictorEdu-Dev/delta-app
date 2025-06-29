package org.deltacore.delta.domains.profile.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import org.deltacore.delta.domains.tutoring.dto.TutoringDTO;
import org.deltacore.delta.domains.profile.model.Roles;

import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
public record UserDTO(
        @Schema(hidden = true)
        Long id,

        @NotBlank(message = "{user.username.notblank}")
        @Size(min = 3, max = 50, message = "{user.username.size}")
        @Pattern(
                regexp = "^[a-zA-Z][a-zA-Z0-9._-]*$",
                message = "{user.username.pattern}"
        )
        String username,

        @Size(max = 255)
        @Pattern(
                regexp = "^[a-zA-Z0-9._%+-]{1,64}@[a-zA-Z0-9.-]{1,255}\\.[a-zA-Z]{2,}$",
                message = "{user.email.format}"
        )
        String email,

        @NotBlank(message = "{user.password.notblank}")
        @Size(min = 6, max = 70, message = "{user.password.size}")
        @Pattern(regexp = ".*[A-Z].*", message = "{user.password.pattern.uppercase}")
        @Pattern(regexp = ".*[a-z].*", message = "{user.password.pattern.lowercase}")
        @Pattern(regexp = ".*\\d.*", message = "{user.password.pattern.digit}")
        @Pattern(regexp = ".*[^a-zA-Z0-9].*", message = "{user.password.pattern.special}")
        String passwordHash,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        LocalDateTime createdAt,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Roles role,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        ProfileDTO profile,

        @Schema(hidden = true)
        List<TutoringDTO>tutorings
) {
        public UserDTO {
                if (email != null) email = email.trim();
        }
}

