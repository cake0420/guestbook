package com.cake7.guestbook.common.scheduler;

import com.cake7.guestbook.mapper.RefreshTokenMapper;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
public class RefreshTokenCleanScheduler {
    private static final Logger logger = LogManager.getLogger();
    private final RefreshTokenMapper refreshTokenMapper;

    @Scheduled(cron = "0 0 0 * * *")
    public void cleanUsedRefreshToken() throws Exception {
        try {
            ZonedDateTime now = ZonedDateTime.now();
            refreshTokenMapper.deleteAllUserTokens(now);
            logger.info("clean used refresh token");
        } catch (Exception e) {
            logger.error("clean used refresh token Exception error {}", e.getMessage());
            throw new Exception("clean used refresh token Exception error :", e);
        }
    }
}
