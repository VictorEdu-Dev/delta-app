package org.deltacore.delta.domains.auth.service;

import jakarta.validation.constraints.NotNull;
import org.deltacore.delta.domains.auth.dto.TokenInfoDTO;

import org.deltacore.delta.domains.auth.dto.LoginRequest;
import org.deltacore.delta.domains.auth.exception.InvalidTokenException;
import org.deltacore.delta.domains.auth.exception.UserNotFound;
import org.deltacore.delta.domains.auth.model.RefreshToken;
import org.deltacore.delta.domains.auth.model.User;
import org.deltacore.delta.domains.auth.repository.RefreshTokenDAO;
import org.deltacore.delta.domains.auth.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthCmdService {
    private static final String DEFAULT_ROLE = "ROLE_STUDENT";
    private UserDAO userDAO;
    private RefreshTokenDAO refreshTokenDAO;

    public TokenInfoDTO getToken(LoginRequest request,
                                 AuthenticationManager authManager,
                                 JwtTokenService jwtService) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        String username = authentication.getName();
        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> new UserNotFound("User not found: " + username));
        String role = Optional.ofNullable(user.getRole())
                .map(r -> "ROLE_" + r.name())
                .orElse(DEFAULT_ROLE);

        return jwtService.generateTokenInfo(authentication.getName(), role);
    }

    public TokenInfoDTO refresh(UUID token, JwtTokenService jwtService) {
        RefreshToken refreshToken = refreshTokenDAO.findByRefreshToken(token)
                .filter(rt -> !rt.isRevoked())
                .filter(rt -> rt.getExpiresAt().isAfter(Instant.now()))
                .orElseThrow(() -> new InvalidTokenException("Invalid or expired refresh token: " + token));

        String username = refreshToken.getUser().getUsername();
        String role = "ROLE_" + refreshToken.getUser().getRole().name();

        refreshToken.setRevoked(true);
        refreshTokenDAO.save(refreshToken);

        return jwtService.generateTokenInfo(username, role);
    }

    public TokenInfoDTO revoke(UUID token) {
        RefreshToken refreshToken = refreshTokenDAO.findByRefreshToken(token)
                .orElseThrow(() -> new InvalidTokenException("Refresh token not found: " + token));

        if (refreshToken.isRevoked()) {
            throw new InvalidTokenException("Refresh token already revoked: " + token);
        }

        refreshToken.setRevoked(true);
        refreshTokenDAO.save(refreshToken);

        return TokenInfoDTO
                .builder()
                .meta("Refresh token revoked successfully.")
                .build();
    }

    @Autowired @Lazy
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired @Lazy
    public void setRefreshTokenDAO(RefreshTokenDAO refreshTokenDAO) {
        this.refreshTokenDAO = refreshTokenDAO;
    }
}
