package com.cake7.guestbook.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;

public interface JwtService {
    String generateAccessToken(Authentication authentication);
    String generateRefreshToken(Authentication authentication);
    Claims parseToken(String token);
    boolean validateToken(String token);
}
