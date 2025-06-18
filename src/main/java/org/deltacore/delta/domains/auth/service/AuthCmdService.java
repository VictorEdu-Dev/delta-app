package org.deltacore.delta.domains.auth.service;

import org.deltacore.delta.domains.auth.dto.TokenInfoDTO;

import org.deltacore.delta.domains.auth.dto.LoginRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthCmdService {
    private static final String DEFAULT_ROLE = "ROLE_STUDENT";
    public TokenInfoDTO getToken(LoginRequest request,
                                 AuthenticationManager authManager,
                                 JwtTokenService jwtService) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        return jwtService.generateTokenInfo(authentication.getName(), DEFAULT_ROLE);
    }
}
