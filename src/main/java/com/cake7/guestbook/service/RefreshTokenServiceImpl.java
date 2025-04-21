package com.cake7.guestbook.service;

import com.cake7.guestbook.domain.RefreshToken;
import com.cake7.guestbook.dto.RefreshTokenResponseDTO;
import com.cake7.guestbook.exception.TokenException;
import com.cake7.guestbook.mapper.RefreshTokenMapper;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private static final Logger logger = LogManager.getLogger(RefreshTokenService.class);
    private final RefreshTokenMapper refreshTokenMapper;
    private final JwtServiceImpl jwtService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createRefreshToken(Authentication authentication) throws Exception {
        try {
            String userId = authentication.getName();

            String refreshTokenId = UUID.randomUUID().toString();
            ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
            ZonedDateTime expiresAt = now.plusDays(1); // 14일 유효기간

            RefreshToken refreshToken = RefreshToken.builder()
                    .id(refreshTokenId)
                    .userId(userId)
                    .createdAt(now)
                    .expiresAt(expiresAt)
                    .used(false)
                    .build();

            refreshTokenMapper.save(refreshToken);
            return refreshTokenId;
        } catch (Exception e) {
            logger.error("during create refresh token {}", e.getMessage());
            throw new Exception("during create refresh token error: "+ e.getMessage());
        }
    }

    //RTR 기법 - 리프레시 토큰 로테이션
    @Transactional
    public RefreshTokenResponseDTO regenerateAccessAndRefreshToken(String refreshTokenId) throws Exception {
        try {
            RefreshToken refreshToken = refreshTokenMapper.findById(refreshTokenId)
                    .orElseThrow(() -> new TokenException("refresh token not found"));

            // 토큰이 이미 사용됐다면 토큰 탈취 가능성 (RTR 핵심 기능)
            if(refreshToken.isUsed()) {
                logger.warn("Token reuse detected for user: {}", refreshToken.getUserId());
                refreshTokenMapper.invalidateAllUserTokens(refreshToken.getUserId());
                throw new TokenException("Token reuse detected for user: "+ refreshToken.getUserId());
            }

            if (refreshToken.isExpired()) {
                logger.warn("Expired token used for user: {}", refreshToken.getUserId());
                throw new TokenException("refresh token expired");
            }

            // 토큰 사용처리
            refreshToken.markUsed();
            refreshTokenMapper.updateUsedStatus(refreshToken.getUserId(), true);

            // 새 액세스 토큰 생성
            Authentication authentication = jwtService.getAuthentication(refreshToken.getUserId());
            String newAccessToken = jwtService.generateAccessToken(authentication);

            String newRefreshToken = createRefreshToken(authentication);

            logger.debug("Refreshed tokens for user: {}", refreshToken.getUserId());
            return new RefreshTokenResponseDTO(newAccessToken, newRefreshToken,
                    "Bearer", 60 * 60);

        } catch (Exception e) {
            logger.error("during get refresh token {}", e.getMessage());
            throw new Exception("during get refresh token error: "+ e.getMessage());
        }
    }
}
