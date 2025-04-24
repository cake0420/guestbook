package com.cake7.guestbook.common.oauth.userInfoAssistant;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class KakaoOAuth2UserInfo implements Oauth2UserInfo{
    private final Map<String, Object> attributes;

    @Override
    public String getId() {
        return "";
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getEmail() {
        return "";
    }

    @Override
    public String getImageUrl() {
        return "";
    }
}
