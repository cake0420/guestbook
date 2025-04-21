package com.cake7.guestbook.mapper;

import com.cake7.guestbook.domain.RefreshToken;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Mapper
@Repository
public interface RefreshTokenMapper {

    @Insert("""
        INSERT INTO refresh_token(id, user_id, created_at, expired_at, used)\s
        VALUES (#{id}, #{userId}, #{createdAt}, #{expiredAt}, #{used})\s
   \s""")
    void save(RefreshToken refreshToken);

    @Select("""
        SELECT re.id
        FROM refresh_token re
        INNER JOIN user u ON u.id = re.user_id
        WHERE provider_id = #{providerId}
        LIMIT 1
    """)
    Optional<RefreshToken> findById(String providerId);

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
