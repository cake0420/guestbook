    package com.cake7.guestbook.domain;

    import lombok.Builder;
    import lombok.Getter;
    import lombok.RequiredArgsConstructor;

    import java.time.ZonedDateTime;

    @Getter
    @Builder
    @RequiredArgsConstructor
    public class User{
        private final String id;
        private final String provider;
        private final String providerId;
        private final String userNameAttribute;
        private final String email;
        private final String name;
        private final String role;
        private final String profile_image_url;
        private final ZonedDateTime createdAt;
        private final ZonedDateTime updatedAt;

    }
