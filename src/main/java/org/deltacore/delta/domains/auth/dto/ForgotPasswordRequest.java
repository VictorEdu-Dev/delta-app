package org.deltacore.delta.domains.auth.dto;

import jakarta.validation.constraints.Email;

public record ForgotPasswordRequest(
    @Email
    String email
) {
}
