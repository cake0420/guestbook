package com.cake7.guestbook.domain.common;

import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class BaseEntity {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}