package org.deltacore.delta.domains.auth.service;

import org.deltacore.delta.domains.auth.exception.UserNotFound;
import org.deltacore.delta.domains.auth.model.Blacklist;
import org.deltacore.delta.domains.auth.model.User;
import org.deltacore.delta.domains.auth.repository.BlacklistDAO;
import org.deltacore.delta.domains.auth.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenBlacklistService {
    private final BlacklistDAO blacklistDAO;
    private final UserDAO userDAO;

    @Autowired
    public TokenBlacklistService(BlacklistDAO blacklistDAO, UserDAO userDAO) {
        this.blacklistDAO = blacklistDAO;
        this.userDAO = userDAO;
    }

    public boolean isBlacklisted(String token, String username) {
        return blacklistDAO.findByToken(token, username).isPresent();
    }

    public void add(String token, Instant expiresAt, String username) {
        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> new UserNotFound("User not found with username: " + username));
        Blacklist blacklist = Blacklist
                .builder()
                .token(token)
                .expiryDate(expiresAt)
                .user(user)
                .build();
        blacklistDAO.save(blacklist);
    }

    public void removeExpiredTokens() {
        Instant now = Instant.now();
        blacklistDAO.deleteAll(blacklistDAO.findAllByExpiryDateBefore(now));
    }
}
