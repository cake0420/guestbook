package com.cake7.guestbook.dto;

public record RefreshTokenResponseDTO(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiredIn
) { }
