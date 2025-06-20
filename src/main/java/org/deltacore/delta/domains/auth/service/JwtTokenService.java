package org.deltacore.delta.domains.auth.service;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.deltacore.delta.domains.auth.dto.RefreshTokenMapper;
import org.deltacore.delta.domains.auth.dto.TokenInfoDTO;
import org.deltacore.delta.domains.auth.dto.UserBasicDTO;
import org.deltacore.delta.domains.auth.dto.UserBasicMapper;
import org.deltacore.delta.domains.auth.exception.UserNotFound;
import org.deltacore.delta.domains.auth.model.RefreshToken;
import org.deltacore.delta.domains.auth.model.User;
import org.deltacore.delta.domains.auth.repository.RefreshTokenDAO;
import org.deltacore.delta.domains.auth.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class JwtTokenService {
    private static final String ISSUER = "DELTA APPLICATION";
    private final JwtEncoder jwtEncoder;
    private UserDAO userDAO;
    private UserBasicMapper userBasicMapper;
    private RefreshTokenDAO refreshTokenDAO;
    private RefreshTokenMapper refreshTokenMapper;

    public JwtTokenService(JWKSource<SecurityContext> jwkSource) {
        this.jwtEncoder = new NimbusJwtEncoder(jwkSource);
    }

    public TokenInfoDTO generateTokenInfo(String username, String role) throws UserNotFound {
        Instant now = Instant.now();
        Instant accessTokenExpiresAt = now.plusSeconds(900);
        Instant refreshTokenExpiresAt = now.plus(7, DAYS);

        String accessToken = jwtEncoder.encode(JwtEncoderParameters.from(
                JwtClaimsSet.builder()
                        .issuer(ISSUER)
                        .issuedAt(now)
                        .expiresAt(accessTokenExpiresAt)
                        .subject(username)
                        .claim("role", role)
                        .build()
        )).getTokenValue();

        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> new UserNotFound("User not found: " + username));
        UserBasicDTO userBasicDTO = userBasicMapper.toDTO(user);

        TokenInfoDTO tokenInfoDTO = new TokenInfoDTO(
                "access_token",
                TokenInfoDTO.RefreshTokenDTO.builder()
                        .userbasicDTO(userBasicDTO)
                        .token(UUID.randomUUID())
                        .revoked(false)
                        .createdAt(now)
                        .expiresAt(refreshTokenExpiresAt)
                        .build(),
                TokenInfoDTO.TokenInfoValueDTO.builder()
                        .username(username)
                        .token(accessToken)
                        .expiresAt(accessTokenExpiresAt)
                        .build()
        );

        RefreshToken refreshToken = refreshTokenMapper.toEntity(tokenInfoDTO.refreshTokenDTO());
        refreshToken.setUser(user);
        refreshTokenDAO.save(refreshToken);

        return tokenInfoDTO;
    }

    @Autowired
    public void setRefreshTokenDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setUserMapper(UserBasicMapper userBasicMapper) {
        this.userBasicMapper = userBasicMapper;
    }

    @Autowired
    public void setRefreshTokenDAO(RefreshTokenDAO refreshTokenDAO) {
        this.refreshTokenDAO = refreshTokenDAO;
    }

    @Autowired
    public void setRefreshTokenMapper(RefreshTokenMapper refreshTokenMapper) {
        this.refreshTokenMapper = refreshTokenMapper;
    }
}

