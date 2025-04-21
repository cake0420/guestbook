package com.cake7.guestbook.service;

import com.cake7.guestbook.domain.User;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>  {
    private static final Logger logger = LogManager.getLogger(CustomOAuth2UserService.class.getName());
    private final UserServiceImpl userServiceImpl;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId(); // "google"
        String providerId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String profileImage = oAuth2User.getAttribute("picture");

        String userNameAttribute = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        if (Objects.requireNonNull(providerId).isEmpty() ||
                Objects.requireNonNull(email).isEmpty()) {
            logger.error("provider or email is empty");
            throw new OAuth2AuthenticationException(new OAuth2Error("Missing essential user info from OAuth provider"));
        }
        // 기존 사용자 조회
        ZonedDateTime utcNow = ZonedDateTime.now(ZoneOffset.UTC);

        User newUser = User.builder()
                        .id(UUID.randomUUID().toString())
                        .provider(provider)
                        .providerId(providerId)
                        .email(email)
                        .role("ROLE_USER")
                        .profile_image_url(profileImage)
                        .createdAt(utcNow)
                        .updatedAt(utcNow)
                        .build();

        try {
            userServiceImpl.updateOrSaveUser(newUser);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Spring Security 세션에 저장할 사용자 객체 리턴
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(newUser.getRole())),
                oAuth2User.getAttributes(),
                userNameAttribute
        );
    }
}
