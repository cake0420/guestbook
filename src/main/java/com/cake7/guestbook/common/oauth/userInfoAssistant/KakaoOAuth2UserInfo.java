package com.cake7.guestbook.common.oauth.userInfoAssistant;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class KakaoOAuth2UserInfo implements Oauth2UserInfo{
    private final Map<String, Object> attributes;

    @Override
    public String getId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getName() {
        return (String) ((Map) attributes.get("properties")).get("nickname");
    }

    @Override
    public String getEmail() {
        return (String) ((Map) attributes.get("kakao_account")).get("email");
    }

    @Override
    public String getImageUrl() {
        return (String) ((Map) attributes.get("properties")).get("profile_image");
    }
}
