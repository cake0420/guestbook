package com.cake7.guestbook.common.dto;

public record RefreshTokenResponseDTO(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiredIn
) { }
