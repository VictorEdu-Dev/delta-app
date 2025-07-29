package org.deltacore.delta.domains.auth.service.internal;

import org.deltacore.delta.domains.auth.repository.RefreshTokenDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RefreshTokenCleanupService {
    private static final int BATCH_SIZE = 500;

    private RefreshTokenDAO refreshTokenRepository;

    @Scheduled(cron = "0 */15 * * * *")
    public void cleanRevokedTokensInBatches() {
        int page = 0;
        List<Long> tokenIds;

        do {
            PageRequest pageRequest = PageRequest.of(page, BATCH_SIZE);
            tokenIds = refreshTokenRepository.findRevokedTokenIds(pageRequest);
            if (!tokenIds.isEmpty()) {
                refreshTokenRepository.deleteByIdIn(tokenIds);
            }
            page++;
        } while (tokenIds.size() == BATCH_SIZE);
    }

    @Scheduled(cron = "0 */15 * * * * ")
    public void setRevokedStatusForOverdueTokens() {
        refreshTokenRepository.markExpiredTokensAsRevoked();
    }

    @Autowired
    private void setRefreshTokenRepository(RefreshTokenDAO refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }
}
