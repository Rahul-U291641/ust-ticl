package com.ticl.auth.scheduler;

import com.ticl.auth.repository.BlacklistedTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BlacklistCleanupJob {
    @Autowired
    BlacklistedTokenRepository blacklistedTokenRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void cleanupExpiredTokens() {
        blacklistedTokenRepository.deleteExpiredTokens(
                LocalDateTime.now());
    }
}
