package com.cake7.guestbook.mapper;

import com.cake7.guestbook.domain.RefreshToken;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Mapper
@Repository
public interface RefreshTokenMapper {
    @Insert("""
        INSERT INTO refresh_token(id, user_id, created_at, expired_at)\s
        VALUES (#{id}, #{userId}, #{createdAt}, #{expiredAt})
   \s""")
    void save(RefreshToken refreshToken);

    @Select("""
        SELECT * FROM refresh_token WHERE id = #{id}")"
    """)
    Optional<RefreshToken> findById(String id);

    @Update("""
        UPDATE refresh_token
        SET used = true
        WHERE user_id = #{userId} AND used = false
    """)
    void invalidateAllUserTokens(String userId);

    @Update("""
        UPDATE refresh_token
        SET used = #{used}
        WHERE id = #{id}
    """)
    void updateUsedStatus(@Param("id") String id, @Param("used") boolean used);

}
