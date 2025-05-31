package org.deltacore.delta.dto.user;

import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record UserDTO(
        @NotBlank(message = "{user.username.notblank}")
        @Size(min = 3, max = 50, message = "{user.username.size}")
        @Pattern(
                regexp = "^[a-zA-Z][a-zA-Z0-9._-]*$",
                message = "{user.username.pattern}"
        )
        String username,

        @Pattern(
                regexp = "^[a-zA-Z0-9._%+-]{1,64}@[a-zA-Z0-9.-]{1,255}\\.[a-zA-Z]{2,}$",
                message = "{user.email.format}"
        )
        String email,

        @NotBlank(message = "{user.password.notblank}")
        @Size(min = 6, message = "{user.password.size}")
        @Pattern(regexp = ".*[A-Z].*", message = "{user.password.pattern.uppercase}")
        @Pattern(regexp = ".*[a-z].*", message = "{user.password.pattern.lowercase}")
        @Pattern(regexp = ".*\\d.*", message = "{user.password.pattern.digit}")
        @Pattern(regexp = ".*[^a-zA-Z0-9].*", message = "{user.password.pattern.special}")
        String passwordHash
) {}

