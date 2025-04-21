package com.cake7.guestbook.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {
    private String id;
    private String userId;
    private ZonedDateTime createdAt;
    private ZonedDateTime expiredAt;
    private boolean used;

    public boolean isExpired() {
        return expiredAt.isBefore(ZonedDateTime.now());
    }

    public void markUsed() {
        this.used = true;
    }
}
