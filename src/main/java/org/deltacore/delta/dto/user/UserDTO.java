package org.deltacore.delta.dto.user;

import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record UserDTO(
        @Size(min = 3, max = 50, message = "{}")
        @Pattern(
                regexp = "^[a-zA-Z][a-zA-Z0-9._-]*$",
                message = "{}"
        )
        String username,

        @Email(message = "{}")
        @Pattern(
                regexp = "^[a-zA-Z0-9._%+-]{1,64}@[a-zA-Z0-9.-]{1,255}\\.[a-zA-Z]{2,}$",
                message = "{}"
        )
        String email,

        @NotBlank()
        @Size(min = 6)
        @Pattern(regexp = ".*[A-Z].*", message = "{}")
        @Pattern(regexp = ".*[a-z].*", message = "{}")
        @Pattern(regexp = ".*\\d.*", message = "{}")
        @Pattern(regexp = ".*[^a-zA-Z0-9].*", message = "{}")
        String passwordHash
) {}

