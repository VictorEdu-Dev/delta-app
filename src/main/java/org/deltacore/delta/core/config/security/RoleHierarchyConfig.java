package org.deltacore.delta.core.config.security;

import org.deltacore.delta.domains.auth.model.Roles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

@Configuration
public class RoleHierarchyConfig {
    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withDefaultRolePrefix()
                .role(Roles.ADMIN.name()).implies(Roles.MONITOR.name())
                .role(Roles.MONITOR.name()).implies(Roles.STUDENT.name())
                .build();
    }
}
