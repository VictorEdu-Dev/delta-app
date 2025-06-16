package org.deltacore.delta.domains.auth.service.event;

import org.deltacore.delta.domains.auth.service.TokenBlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TokenCleanupJob {

    private final TokenBlacklistService blacklistService;

    @Autowired
    public TokenCleanupJob(TokenBlacklistService blacklistService) {
        this.blacklistService = blacklistService;
    }

    @Scheduled(fixedRate = 1_800_000)
    public void cleanExpiredTokens() {
        blacklistService.removeExpiredTokens();
    }
}

