package com.cake7.guestbook.service;

import org.springframework.security.core.Authentication;

public interface RefreshTokenService {
    String createRefreshToken(Authentication authentication) throws Exception;
}
