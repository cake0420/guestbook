    package com.cake7.guestbook.domain;

    import lombok.Builder;
    import lombok.Getter;

    import java.time.ZonedDateTime;

    @Getter
    @Builder
    public class User{
        private final String id;
        private final String provider;
        private final String providerId;
        private final String email;
        private final String name;
        private final String profile_image_url;
        private final String role;
        private final ZonedDateTime createdAt;
        private final ZonedDateTime updatedAt;


        public User(String id, String provider,
                    String providerId, String email, String name, String roleUser, String profileImage,
                    ZonedDateTime createdAt, ZonedDateTime updatedAt) {
            this.id = id;
            this.name = name;
            this.provider = provider;
            this.providerId = providerId;
            this.email = email;
            this.profile_image_url = profileImage;
            this.role = roleUser;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }
    }
