package com.cake7.guestbook.common.service;

import com.cake7.guestbook.common.dto.RefreshTokenResponseDTO;
import com.cake7.guestbook.common.exception.TokenException;
import com.cake7.guestbook.common.oauth.jwtAssistant.OAuth2ProviderStrategy;
import com.cake7.guestbook.common.oauth.jwtAssistant.OAuth2ProviderStrategyFactory;
import com.cake7.guestbook.domain.RefreshToken;
import com.cake7.guestbook.mapper.RefreshTokenMapper;
import com.cake7.guestbook.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private static final Logger logger = LogManager.getLogger(RefreshTokenService.class);
    private final RefreshTokenMapper refreshTokenMapper;
    private final JwtServiceImpl jwtServiceImpl;
    private final UserMapper userMapper;
    private final OAuth2ProviderStrategyFactory strategyFactory;

    @Override
    @Transactional
    public String createRefreshToken(Authentication authentication) throws Exception {
        try {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();

            OAuth2ProviderStrategy strategy = strategyFactory.getStrategy(registrationId);
            String providerId = strategy.extractProviderId(oAuth2User);
            String userId = userMapper.findByProviderId(providerId);

            ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);

            List<RefreshToken> activeTokens = refreshTokenMapper
                    .findByUserAndUsedIsFalseAndExpiredAtAfterOrderByCreatedAtAsc(userId, now);

            final int MAX_TOKENS_PER_USER = 3;
            if (activeTokens.size() >= MAX_TOKENS_PER_USER) {
                // 가장 오래된 토큰부터 삭제 (최신 2개만 남김)
                for (int i = 0; i < activeTokens.size() - (MAX_TOKENS_PER_USER - 1); i++) {
                    refreshTokenMapper.delete(activeTokens.get(i).getId());
                }
            }
            String refreshTokenId = UUID.randomUUID().toString();
            ZonedDateTime expiresAt = now.plusDays(1);
            RefreshToken refreshToken = RefreshToken.builder()
                    .id(refreshTokenId)
                    .userId(userId)
                    .createdAt(now)
                    .expiredAt(expiresAt)
                    .used(false)
                    .build();

            refreshTokenMapper.save(refreshToken);
            return refreshTokenId;
        } catch (Exception e) {
            logger.error("during create refresh token error {}", e.getMessage());
            throw new Exception("during create refresh token error: "+ e.getMessage());
        }
    }

    //RTR 기법 - 리프레시 토큰 로테이션
    @Transactional
    public RefreshTokenResponseDTO regenerateAccessAndRefreshToken(String refreshTokenId) throws Exception {
        try {
            RefreshToken refreshToken = refreshTokenMapper.findByProviderId(refreshTokenId)
                    .orElseThrow(() -> new TokenException("refresh token not found"));

            if (refreshToken.getExpiredAt() == null) {
                logger.warn("ExpiredAt is null for token: {}", refreshTokenId);
                refreshTokenMapper.invalidateAllUserTokens(refreshToken.getUserId());
                throw new TokenException("Invalid token state: expiration date is missing");
            }

            ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
            if(!refreshToken.getExpiredAt().isBefore(now)) {
                logger.warn("not ExpiredAt is before now for token: {}", refreshTokenId);
                refreshTokenMapper.invalidateAllUserTokens(refreshToken.getUserId());
                throw new TokenException("Invalid token state: not yet expired token");
            }

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
            refreshTokenMapper.updateUsedStatus(refreshToken.getId(), true);

            // 새 액세스 토큰 생성
            Authentication authentication = jwtServiceImpl.getAuthentication(refreshToken.getUserId());
            String newAccessToken = jwtServiceImpl.generateAccessToken(authentication);

            String newRefreshToken = createRefreshToken(authentication);

            logger.debug("Refreshed tokens for user: {}", refreshToken.getUserId());
            return new RefreshTokenResponseDTO(newAccessToken, newRefreshToken,
                    "Cookie", 60 * 60 * 24 * 7);

        } catch (TokenException e) {
            logger.error("during regenerate refresh token TokenException error {}", e.getMessage());
            throw new TokenException("during regenerate refresh token error: "+ e.getMessage());
        } catch (Exception e) {
            logger.error("during regenerate refresh token Exception error {}", e.getMessage());
            throw new Exception("during create refresh token error: "+ e.getMessage());
        }
    }
}
