package com.cake7.guestbook.controller;

import com.cake7.guestbook.dto.TestTokenDTO;
import com.cake7.guestbook.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증 테스트", description = "인증 관련 api")
@RestController
@RequestMapping("/api/test/auth")
@RequiredArgsConstructor

public class TestAuthController {
    private final JwtUtils jwtUtils;

    @Operation(summary = "토큰 검증", description = "JWT 토큰의 유효성을 검증합니다.")
    @GetMapping("/validate")
    public ResponseEntity<TestTokenDTO> validateToken(HttpServletRequest request) {
        // Extract token specifically from cookies
        String token = jwtUtils.extractToken(request);

        if (token != null && !token.isEmpty()) {
            boolean isValid = jwtUtils.validateToken(token);
            if (isValid) {
                // If valid, we can also extract and return some user information
                Claims claims = jwtUtils.parseToken(token);
                String email = claims.getSubject();
                String authorities = (String) claims.get("authorities");

                return ResponseEntity.ok(new TestTokenDTO(claims, isValid,
                        "Valid token for user: " + email + " with authorities: " + authorities));
            }
        }

        return ResponseEntity.badRequest().body(new TestTokenDTO(null, false, "Invalid or missing token"));
    }
}
