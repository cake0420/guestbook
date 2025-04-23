package com.cake7.guestbook.service;

import com.cake7.guestbook.domain.User;
import com.cake7.guestbook.domain.enums.Role;
import com.cake7.guestbook.oauth.userInfo.OAuth2UserInfoFactory;
import com.cake7.guestbook.oauth.userInfo.Oauth2UserInfo;
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
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private static final Logger logger = LogManager.getLogger(CustomOAuth2UserService.class.getName());
    private final UserServiceImpl userServiceImpl;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttribute = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        Oauth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oAuth2User.getAttributes());

        String providerId = userInfo.getId();
        String email = userInfo.getEmail();
        String name = userInfo.getName();
        String profileImage = userInfo.getImageUrl();


        if (Objects.requireNonNull(registrationId).isEmpty() || Objects.requireNonNull(providerId).isEmpty()) {
            logger.error("providerId or registrationId is empty");
            throw new OAuth2AuthenticationException(new OAuth2Error("Missing essential user info from OAuth provider"));
        }

        ZonedDateTime utcNow = ZonedDateTime.now(ZoneOffset.UTC);
        User newUser = User.builder()
                .id(UUID.randomUUID().toString())
                .provider(registrationId)
                .providerId(providerId)
                .userNameAttribute(userNameAttribute)
                .email(email)
                .name(name)
                .role(Role.ROLE_USER.name())
                .profile_image_url(profileImage)
                .createdAt(utcNow)
                .updatedAt(utcNow)
                .build();




        try {
            userServiceImpl.updateOrSaveUser(newUser);
        } catch (Exception e) {
            throw new RuntimeException("유저 저장 중 오류 발생", e);
        }

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(newUser.getRole())),
                oAuth2User.getAttributes(),
                userNameAttribute
        );
    }
}
