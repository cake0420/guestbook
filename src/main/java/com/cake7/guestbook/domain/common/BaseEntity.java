package com.cake7.guestbook.domain.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.ZonedDateTime;


@Getter
@RequiredArgsConstructor
public class BaseEntity {
    private final ZonedDateTime createdAt;
    private final ZonedDateTime updatedAt;
}