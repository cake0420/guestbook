package com.cake7.guestbook.common.service;

import com.cake7.guestbook.common.exception.UserNotfoundException;
import org.springframework.security.core.Authentication;

public interface JwtService {
    String generateAccessToken(Authentication authentication) throws Exception;
//    void generateRefreshToken(Authentication authentication);
    Authentication getAuthentication(String subject) throws UserNotfoundException; // 새로 추가된 메소드

}
