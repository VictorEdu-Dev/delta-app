package org.deltacore.delta.domains.profile.model;

public enum Permissions {
    // Monitor permissions
    MONITOR_READ,
    MONITOR_WRITE,
    MONITOR_DELETE,

    // Admin permissions
    ADMIN_READ,
    ADMIN_WRITE,
    ADMIN_DELETE,

    // Student permissions
    STUDENT_READ,

    // Free user permissions
    FREE_READ;
}
