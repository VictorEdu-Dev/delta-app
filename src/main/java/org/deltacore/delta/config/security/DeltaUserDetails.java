package org.deltacore.delta.config.security;

import org.deltacore.delta.dto.user.UserDTO;
import org.deltacore.delta.model.user.Roles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class DeltaUserDetails implements UserDetails {

    private final UserDTO user;

    public DeltaUserDetails(UserDTO userDTO) {
        this.user = userDTO;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        Roles role = user.role();
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    public String getPassword() {
        return user.passwordHash();
    }

    public String getUsername() {
        return user.username();
    }

    public String getEmail() {
        return user.email();
    }

    public Roles getRole() {
        return user.role();
    }
}
