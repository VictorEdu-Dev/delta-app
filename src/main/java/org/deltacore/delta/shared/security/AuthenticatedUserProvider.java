package org.deltacore.delta.shared.security;

import org.deltacore.delta.domains.profile.exception.UserNotFound;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AuthenticatedUserProvider {

    public Optional<AuthenticatedUser> current() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        String username = auth.getName();
        Set<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return Optional.of(new AuthenticatedUser(username, roles));
    }

    public String currentUsername() {
        return current()
                .map(AuthenticatedUser::username)
                .orElseThrow(() -> new UserNotFound("No authenticated user found"));
    }

    public Set<String> currentRoles() {
        return current()
                .map(AuthenticatedUser::roles)
                .orElseThrow(() -> new UserNotFound("No authenticated user found"));
    }
}

