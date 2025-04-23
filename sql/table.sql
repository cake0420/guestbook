CREATE TABLE user (
       id VARCHAR(36) PRIMARY KEY,
       provider VARCHAR(50) NOT NULL,
       provider_id VARCHAR(100) NOT NULL,
       user_name_attribute VARCHAR(50) NOT NULL ,
       email VARCHAR(100),
       name VARCHAR(100),
       role VARCHAR(40) DEFAULT 'ROLE_USER',
       profile_image_url VARCHAR(255),
       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
       updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
       UNIQUE KEY uk_provider_provider_id (provider, provider_id)
);

CREATE TABLE refresh_token (
    id VARCHAR(36) PRIMARY KEY ,
    user_id VARCHAR(36) NOT NULL ,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    expired_at DATETIME  NOT NULL,
    used BOOLEAN NOT NULL DEFAULT FALSE,

    INDEX idx_user_id (user_id),
    INDEX idx_expired_at (expired_at),
    CONSTRAINT fk_token_user_id FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);