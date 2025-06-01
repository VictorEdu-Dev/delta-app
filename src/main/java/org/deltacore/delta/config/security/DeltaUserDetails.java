package org.deltacore.delta.config.security;

import org.deltacore.delta.dto.user.UserDTO;
import org.deltacore.delta.dto.user.UserDeltaMapper;
import org.deltacore.delta.model.user.Permissions;
import org.deltacore.delta.model.user.Roles;
import org.deltacore.delta.model.user.User;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DeltaUserDetails implements UserDetails {

    private final User user;

    public DeltaUserDetails(UserDTO userDTO) {
        UserDeltaMapper userDeltaMapper = Mappers.getMapper(UserDeltaMapper.class);
        this.user = userDeltaMapper.toEntity(userDTO);
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        Roles role = user.getRole();
        System.out.println(role.name());
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    public String getPassword() {
        return user.getPasswordHash();
    }

    public String getUsername() {
        return user.getUsername();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public Roles getRole() {
        return user.getRole();
    }
}
