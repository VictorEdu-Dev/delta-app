package org.deltacore.delta.domains.auth.service;

import jakarta.transaction.Transactional;
import org.deltacore.delta.domains.auth.dto.TokenInfoDTO;
import org.deltacore.delta.domains.auth.model.RefreshToken;
import org.deltacore.delta.domains.auth.model.Roles;
import org.deltacore.delta.domains.auth.model.User;
import org.deltacore.delta.domains.auth.repository.RefreshTokenDAO;
import org.deltacore.delta.domains.auth.repository.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Transactional
@SpringBootTest
class JwtTokenServiceIT {
    JwtTokenService jwtTokenService;
    UserDAO userDAO;
    RefreshTokenDAO refreshTokenDAO;

    Long userId;

    @BeforeEach
    void setup() {
        refreshTokenDAO.deleteAll();
        userDAO.deleteAll();

        User user = new User();
        user.setUsername("testuser");
        user.setPasswordHash("123456");
        user.setEmail("test@delta.com");
        user.setRole(Roles.STUDENT);

        userDAO.save(user);
        userId = user.getId();
    }

    @Test
    void shouldGenerateTokenAndStoreRefreshToken() {
        TokenInfoDTO tokenInfo = jwtTokenService.generateTokenInfo("testuser", "ROLE_USER");

        assertThat(tokenInfo.tokenInfoValue().token()).isNotEmpty();
        assertThat(tokenInfo.refreshTokenDTO().token()).isNotNull();
        assertThat(tokenInfo.refreshTokenDTO().revoked()).isFalse();

        Optional<RefreshToken> saved = refreshTokenDAO.findByRefreshToken(tokenInfo.refreshTokenDTO().token());
        assertThat(saved).isPresent();

        RefreshToken refreshToken = saved.orElseThrow(() -> new AssertionError("RefreshToken não encontrado"));
        assertThat(refreshToken.getUser().getUsername()).isEqualTo("testuser");
    }

    @Autowired
    public void setRefreshTokenDAO(RefreshTokenDAO refreshTokenDAO) {
        this.refreshTokenDAO = refreshTokenDAO;
    }

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setJwtTokenService(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }
}