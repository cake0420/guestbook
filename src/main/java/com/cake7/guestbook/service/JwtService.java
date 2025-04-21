package com.cake7.guestbook.service;

import org.springframework.security.core.Authentication;

public interface JwtService {
    String generateAccessToken(Authentication authentication) throws Exception;
//    void generateRefreshToken(Authentication authentication);
    Authentication getAuthentication(String subject); // 새로 추가된 메소드

}
