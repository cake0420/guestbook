package com.cake7.guestbook.domain;

import com.cake7.guestbook.domain.common.BaseEntity;
import lombok.Getter;

import java.util.UUID;

@Getter
public class User extends BaseEntity {
    private final UUID uuid;
    private final String name;
    private final String provider;
    private final String providerId;
    private final String email;
    private final String profile_image_url;
    private final String role;


    public User(UUID uuid, String name, String provider, String providerId, String email, String profileImage, String roleUser) {
        this.uuid = uuid;
        this.name = name;
        this.provider = provider;
        this.providerId = providerId;
        this.email = email;
        this.profile_image_url = profileImage;
        this.role = roleUser;
    }
}
