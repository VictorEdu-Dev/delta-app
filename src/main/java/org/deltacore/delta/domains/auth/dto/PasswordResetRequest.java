package org.deltacore.delta.domains.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PasswordResetRequest(
        @Email String email,
        @Size(min = 6, max = 6) String code,

        @NotBlank(message = "{user.password.not.blank}")
        @Size(min = 6, message = "{user.password.size}")
        @Pattern(regexp = ".*[A-Z].*", message = "{user.password.pattern.uppercase}")
        @Pattern(regexp = ".*[a-z].*", message = "{user.password.pattern.lowercase}")
        @Pattern(regexp = ".*\\d.*", message = "{user.password.pattern.digit}")
        @Pattern(regexp = ".*[^a-zA-Z0-9].*", message = "{user.password.pattern.special}")
        String newPassword
) {
}
