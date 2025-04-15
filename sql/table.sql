CREATE TABLE user (
       id VARCHAR(36) PRIMARY KEY,
       provider VARCHAR(50) NOT NULL,
       provider_id VARCHAR(100) NOT NULL,
       email VARCHAR(100),
       name VARCHAR(100),
       role VARCHAR(40) DEFAULT 'ROLE_USER',
       profile_image_url VARCHAR(255),
       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
       updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
       UNIQUE KEY uk_provider_provider_id (provider, provider_id)
);
