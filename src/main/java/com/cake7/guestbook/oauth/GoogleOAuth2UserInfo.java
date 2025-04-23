package com.cake7.guestbook.oauth;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class GoogleOAuth2UserInfo implements Oauth2UserInfo{

    private final Map<String, Object> attributes;

    @Override
    public String getId() {
        return (String) attributes.get("sub");
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
        return (String) attributes.get("picture");
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of(
                "sub", getId(),
                "name", getName(),
                "email", getEmail(),
                "picture", getImageUrl()
        );
    }
}
