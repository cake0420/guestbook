package com.cake7.guestbook.oauth;

import com.cake7.guestbook.domain.enums.SocialPlatform;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static Oauth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(SocialPlatform.GOOGLE.name())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if(registrationId.equalsIgnoreCase(SocialPlatform.NAVER.name())) {
            return new NaverOAuth2UserInfo(attributes);
        } else {
            throw new IllegalArgumentException("Unsupported social platform: " + registrationId);
        }
    }
}
