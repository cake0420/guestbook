package com.cake7.guestbook.service;

import com.cake7.guestbook.config.JwtConfig;
import com.cake7.guestbook.domain.User;
import com.cake7.guestbook.exception.TokenException;
import com.cake7.guestbook.exception.UserNotfoundException;
import com.cake7.guestbook.mapper.UserMapper;
import com.cake7.guestbook.oauth.jwt.OAuth2ProviderStrategy;
import com.cake7.guestbook.oauth.jwt.OAuth2ProviderStrategyFactory;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final JwtConfig jwtConfig;
    private final UserMapper userMapper;
    private static final Logger logger = LogManager.getLogger();
    private final OAuth2ProviderStrategyFactory strategyFactory;

    @Override
    public String generateAccessToken(Authentication authentication) throws Exception {
        try {

            String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

            OAuth2ProviderStrategy strategy = strategyFactory.getStrategy(registrationId);
            String providerId = strategy.extractProviderId(oAuth2User);

            String authorities = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
            ZonedDateTime expiration = now.plus(jwtConfig.getExpiration(), ChronoUnit.MILLIS);

            return Jwts.builder()
                    .subject(providerId)
                    .claim("authorities",authorities)
                    .issuedAt(Date.from(now.toInstant()))
                    .expiration(Date.from(expiration.toInstant()))
                    .issuer("cake7-auth-server") // ✅ 발급자 설정
                    .audience().add("cake7-client").and()
                    .signWith(jwtConfig.secretKey(), Jwts.SIG.HS256)
                    .compact();
        } catch (Exception e) {
            logger.error("during generate access token {}", e.getMessage());
            throw new Exception("during generate access token: "+ e.getCause());
        }


    }

    @Override
    public Authentication getAuthentication(String userId) throws UserNotfoundException, TokenException {
        try {
            // Fetch user details from the database using the user ID
            User user = userMapper.findById(userId);

            if (user == null) {
                throw new UserNotfoundException("User not found for ID: " + userId);
            }

            // Create authorities based on the user's role
            List<SimpleGrantedAuthority> authorities =
                    Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));

            OAuth2ProviderStrategy strategy = strategyFactory.getStrategy(user.getProvider());
            Map<String, Object> attributes = strategy.buildUserAttributes(user);

            // Create OAuth2User
            OAuth2User oAuth2User = new DefaultOAuth2User(
                    authorities,
                    attributes,
                    user.getUserNameAttribute()
            );

            // Return authentication with OAuth2User as principal
            return new UsernamePasswordAuthenticationToken(oAuth2User, null, authorities);

        } catch (Exception e) {
            logger.error("Error getting authentication: {}", e.getMessage());
            throw new TokenException("Error getting authentication: " + e.getMessage());
        }
    }
}
