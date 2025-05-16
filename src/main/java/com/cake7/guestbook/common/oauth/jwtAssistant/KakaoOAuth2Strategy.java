package com.cake7.guestbook.common.oauth.jwtAssistant;

import com.cake7.guestbook.domain.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
public class KakaoOAuth2Strategy implements OAuth2ProviderStrategy {
    @Override
    public String extractProviderId(OAuth2User oAuth2User) {
        return Objects.requireNonNull(oAuth2User.getAttribute("id")).toString();
    }

    @Override
    public Map<String, Object> buildUserAttributes(User user) {
        return Map.of("id", user.getProviderId(),
                "profile_nickname", user.getName(),
                "profile_image", user.getProfile_image_url());
    }
}