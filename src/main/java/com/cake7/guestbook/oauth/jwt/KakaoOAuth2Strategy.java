package com.cake7.guestbook.oauth.jwt;

import com.cake7.guestbook.domain.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KakaoOAuth2Strategy implements OAuth2ProviderStrategy {
    @Override
    public String extractProviderId(OAuth2User oAuth2User) {
        return String.valueOf(oAuth2User.getAttribute("id"));
    }

    @Override
    public Map<String, Object> buildUserAttributes(User user) {
        return Map.of("미정", user.getProviderId(),
                    "name", user.getName(),
                    "email", user.getEmail(),
                    "미정", user.getProfile_image_url());
    }
}