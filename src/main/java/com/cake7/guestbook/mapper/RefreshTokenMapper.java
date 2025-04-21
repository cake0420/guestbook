package com.cake7.guestbook.mapper;

import com.cake7.guestbook.domain.RefreshToken;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RefreshTokenMapper {
    @Insert("""
        INSERT INTO refresh_token(id, user_id, created_at, expired_at)\s
        VALUES (#{id}, #{userId}, #{createdAt}, #{expiredAt})
   \s""")
    void save(RefreshToken refreshToken);
}
