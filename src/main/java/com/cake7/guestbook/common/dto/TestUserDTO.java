package com.cake7.guestbook.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 정보를 담는 DTO")
public record TestUserDTO(
        @Schema(description = "사용자의 이름", example = "Alice")
        String name,

        @Schema(description = "사용자의 나이", example = "25")
        int age
) {}
