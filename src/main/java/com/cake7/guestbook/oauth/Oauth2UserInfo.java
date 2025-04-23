package com.cake7.guestbook.oauth;

import java.util.Map;

public interface Oauth2UserInfo {

    String getId();
    String getName();
    String getEmail();
    String getImageUrl();

    Map<String, Object> getAttributes();
}
