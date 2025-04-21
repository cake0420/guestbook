package com.cake7.guestbook.service;

import com.cake7.guestbook.config.JwtConfig;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final JwtConfig jwtConfig;
    private static final Logger logger = LogManager.getLogger();

    @Override
    public String generateAccessToken(Authentication authentication) throws Exception {
        try {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String providerId = oAuth2User.getAttribute("sub");
            String authorities = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
            ZonedDateTime expiration = now.plus(jwtConfig.getExpiration(), ChronoUnit.MILLIS);
            System.out.println("OAuth2User attributes: " + oAuth2User.getAttributes());
            System.out.println("Authorities: " + authentication.getAuthorities());

            return Jwts.builder()
                    .subject(providerId)
                    .claim("authorities",authorities)
                    .issuedAt(Date.from(now.toInstant()))
                    .expiration(Date.from(expiration.toInstant()))
                    .issuer("cake7-auth-server") // ✅ 발급자 설정
                    .audience().add("cake7-client").and()
                    .signWith(jwtConfig.secretKey())
                    .compact();
        } catch (Exception e) {
            logger.error("during generate access token {}", e.getMessage());
            throw new Exception("during generate access token: "+ e.getCause());
        }


    }

    @Override
    public Authentication getAuthentication(String subject) {
        // 리프레시 토큰에서 사용자 인증 객체 생성 (userId = subject)
        // 실제 구현에서는 UserService를 통해 DB에서 사용자 정보와 권한을 조회할 수 있음
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        return new UsernamePasswordAuthenticationToken(subject, null, authorities);
    }
}
