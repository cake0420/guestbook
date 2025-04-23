package com.cake7.guestbook.oauth;

import java.util.Map;

public class NaverOAuth2UserInfo implements Oauth2UserInfo{

    private final Map<String, Object> attributes;

    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = (Map<String, Object>) attributes.get("response");
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

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of(
                "id", getId(),
                "name", getName(),
                "email", getEmail(),
                "profile_image", getImageUrl()
        );
    }
}
