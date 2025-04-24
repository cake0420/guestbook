package com.cake7.guestbook.common.oauth.userInfoAssistant;

import java.util.Map;

public class NaverOAuth2UserInfo implements Oauth2UserInfo{

    private final Map<String, Object> attributes;

    @SuppressWarnings("unchecked")
    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        Object response = attributes.get("response");
        if (response instanceof Map<?, ?> map) {
            this.attributes = (Map<String, Object>) map;
        } else {
            throw new IllegalArgumentException("Naver OAuth response is not a valid map.");
        }
    }


    @Override
    public String getId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("profile_image");
    }

}
