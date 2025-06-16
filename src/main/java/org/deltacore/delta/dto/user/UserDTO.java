package org.deltacore.delta.dto.user;

import jakarta.validation.constraints.*;
import lombok.Builder;
import org.deltacore.delta.dto.tutoring.TutoringDTO;
import org.deltacore.delta.model.user.Roles;

import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
public record UserDTO(
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

        LocalDateTime createdAt,

        Roles role,

        ProfileDTO profile,

        List<TutoringDTO>tutorings
) {}

