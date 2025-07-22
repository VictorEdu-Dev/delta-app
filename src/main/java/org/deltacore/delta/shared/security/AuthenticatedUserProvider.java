package org.deltacore.delta.shared.security;

import org.deltacore.delta.domains.profile.exception.UserNotFoundException;
import org.deltacore.delta.domains.profile.model.User;
import org.deltacore.delta.domains.profile.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    private UserDAO userDAO;

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
                .orElseThrow(() -> new UserNotFoundException("No authenticated user found"));
    }

    public Set<String> currentRoles() {
        return current()
                .map(AuthenticatedUser::roles)
                .orElseThrow(() -> new UserNotFoundException("No authenticated user found"));
    }

    public User currentUser() {
        return userDAO.findByUsername(currentUsername())
                .orElseThrow(() -> new UserNotFoundException("No authenticated user found with username: " + currentUsername()));
    }

    @Autowired @Lazy
    private void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
}

