package com.cake7.guestbook.controller;

import com.cake7.guestbook.common.dto.RefreshTokenResponseDTO;
import com.cake7.guestbook.common.service.RefreshTokenServiceImpl;
import com.cake7.guestbook.common.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@Tag(name = "Authentication", description = "인증 관련 API")
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtils jwtUtils;
    private final RefreshTokenServiceImpl refreshTokenServiceImpl;

    @Operation(summary = "토큰 갱신", description = "리프레시 토큰을 사용하여 액세스 토큰과 리프레시 토큰을 갱신합니다.")
    @GetMapping("/refresh")
    public ResponseEntity<RefreshTokenResponseDTO> refresh(HttpServletRequest request) {
        try {
            String token = jwtUtils.extractToken(request);
            if (token != null && !token.isEmpty()) {
                boolean isValid = jwtUtils.validateToken(token);
                if (isValid) {
                    // If valid, we can also extract and return some user information
                    Claims claims = jwtUtils.parseToken(token);
                    String id = (String) claims.get("sub");
                    RefreshTokenResponseDTO response = refreshTokenServiceImpl.regenerateAccessAndRefreshToken(id);
                    ResponseCookie cookie = ResponseCookie.from("jwt_token", response.accessToken())
                            .httpOnly(true)
                            .secure(true)
                            .path("/")
                            .maxAge(Duration.ofHours(1))
                            .sameSite("Strict")  // 이게 핵심!
                            .build();
                    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(response);
                }
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}