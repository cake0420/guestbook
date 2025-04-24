package com.cake7.guestbook.common.service;

import com.cake7.guestbook.common.dto.RefreshTokenResponseDTO;
import org.springframework.security.core.Authentication;

public interface RefreshTokenService {
    String createRefreshToken(Authentication authentication) throws Exception;
    RefreshTokenResponseDTO regenerateAccessAndRefreshToken(String refreshTokenId) throws Exception;
}
