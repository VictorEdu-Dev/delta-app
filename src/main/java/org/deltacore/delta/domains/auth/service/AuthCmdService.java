package org.deltacore.delta.domains.auth.service;

import org.deltacore.delta.domains.auth.model.DeltaUserDetails;
import org.deltacore.delta.domains.auth.dto.LoginRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class AuthCmdService {
    public String getToken(LoginRequest request,
                           AuthenticationManager authManager,
                           JwtTokenService jwtService) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        DeltaUserDetails userDetails = (DeltaUserDetails) authentication.getPrincipal();

        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_STUDENT");

        return jwtService.generateToken(authentication.getName(), role);
    }
}
