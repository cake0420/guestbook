package com.cake7.guestbook.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.ZonedDateTime;

@Getter
@RequiredArgsConstructor
public class RefreshToken {
    private final String id;
    private final String userId;
    private final ZonedDateTime createdAt;
    private final ZonedDateTime expiresAt;
}
