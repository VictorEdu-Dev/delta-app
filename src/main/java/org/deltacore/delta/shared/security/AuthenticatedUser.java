package org.deltacore.delta.shared.security;

import java.util.Set;

public record AuthenticatedUser(String username, Set<String> roles) {}
