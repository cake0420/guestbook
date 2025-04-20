package com.cake7.guestbook.controller;

import com.cake7.guestbook.dto.TestTokenDTO;
import com.cake7.guestbook.service.JwtServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증 테스트", description = "인증 관련 api")
@RestController
@RequestMapping("/api/test/auth")
@RequiredArgsConstructor

public class TestAuthController {
    private final JwtServiceImpl jwtService;

    @Operation(summary = "토큰 검증", description = "JWT 토큰의 유효성을 검증합니다.")
    @GetMapping("/validate")
    public ResponseEntity<TestTokenDTO> validateToken(Authentication authentication) {
        String token = jwtService.generateToken(authentication);
        if (token != null && !token.isEmpty()) {
            return ResponseEntity.ok(new TestTokenDTO(true, "valid"));
        }
        return  ResponseEntity.badRequest().body(new TestTokenDTO(false, "invalid"));
    }
}
