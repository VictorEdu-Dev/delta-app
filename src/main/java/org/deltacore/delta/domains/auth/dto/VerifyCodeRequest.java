package org.deltacore.delta.domains.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;

public record VerifyCodeRequest(
        @Positive
        String code,
        @Email
        String email
) {
}
