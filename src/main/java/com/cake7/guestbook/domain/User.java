    package com.cake7.guestbook.domain;

    import com.cake7.guestbook.domain.common.BaseEntity;
    import lombok.Getter;

    import java.time.LocalDateTime;

    @Getter
    public class User extends BaseEntity {
        private final String id;
        private final String provider;
        private final String providerId;
        private final String email;
        private final String name;
        private final String profile_image_url;
        private final String role;


        public User(String id, String name, String provider,
                    String providerId, String email, String profileImage, String roleUser,
                    LocalDateTime createdAt, LocalDateTime updatedAt) {
            super(createdAt, updatedAt);
            this.id = id;
            this.name = name;
            this.provider = provider;
            this.providerId = providerId;
            this.email = email;
            this.profile_image_url = profileImage;
            this.role = roleUser;
        }
    }
