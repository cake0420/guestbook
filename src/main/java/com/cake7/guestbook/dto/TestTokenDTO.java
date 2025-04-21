package com.cake7.guestbook.dto;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "토큰 응답 DTO")
public record TestTokenDTO(
        @Schema(description = "claim 여부")
        Claims claims,

        @Schema(description = "토큰 유효성 여부", example = "true")
        boolean valid,

        @Schema(description = "메시지", example = "유효한 토큰입니다.")
        String message
) {}
