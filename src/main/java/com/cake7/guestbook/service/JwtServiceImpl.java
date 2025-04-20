package com.cake7.guestbook.service;

import com.cake7.guestbook.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final JwtConfig jwtConfig;
    private static final Logger logger = LogManager.getLogger();

    @Override
    public String generateToken(Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
//        String subject = oAuth2User.getAttribute("sub");
        String authorities = oAuth2User.getAttribute("authorities");

        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        ZonedDateTime expiration = now.plus(jwtConfig.getExpiration(), ChronoUnit.MILLIS);


        return Jwts.builder()
                .subject(email)
                .claim("email",email)
//                .claim("sub",subject)
                .claim("authorities",authorities)
                .issuedAt(Date.from(now.toInstant()))
                .expiration(Date.from(expiration.toInstant()))
                .signWith(jwtConfig.secretKey())
                .compact();
    }

    @Override
    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(jwtConfig.secretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            logger.error("parse token error: {}", e.getMessage());
            throw new JwtException("parse token error");
        }
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(jwtConfig.secretKey());
            return true;
        } catch (Exception e) {
            logger.error("during validate token error: {}", e.getMessage());
            return false;
        }
    }
}
