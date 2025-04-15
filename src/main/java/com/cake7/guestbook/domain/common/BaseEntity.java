package com.cake7.guestbook.domain.common;

import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class BaseEntity {
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public BaseEntity(LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}