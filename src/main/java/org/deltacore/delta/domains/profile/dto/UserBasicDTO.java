package org.deltacore.delta.domains.profile.dto;

import org.deltacore.delta.domains.profile.model.Roles;

public record UserBasicDTO(
        Long id,
        String username,
        Roles role
) {
}
