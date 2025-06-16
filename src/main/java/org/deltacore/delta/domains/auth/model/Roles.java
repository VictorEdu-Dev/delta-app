package org.deltacore.delta.domains.auth.model;

import lombok.Getter;

import java.util.Set;

@Getter
public enum Roles {
    ADMIN(Set.of(Permissions.ADMIN_READ,
                 Permissions.ADMIN_WRITE,
                 Permissions.ADMIN_DELETE)),
    MONITOR(Set.of(Permissions.MONITOR_READ,
                 Permissions.MONITOR_WRITE,
                 Permissions.MONITOR_DELETE)),
    STUDENT(Set.of(Permissions.STUDENT_READ)),
    FREE(Set.of(Permissions.FREE_READ));

    private final Set<Permissions> permissions;

    Roles(Set<Permissions> permissions) {
        this.permissions = permissions;
    }
}
