package com.cake7.guestbook.mapper;

import com.cake7.guestbook.domain.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;

@Mapper
@Repository
public interface UserMapper {
    @Select("""
        SELECT *
        FROM user
        WHERE id = #{id}
    """)
    User findById(@Param("id") String id);

    @Select("""
        SELECT id
        FROM user
        WHERE provider_id = #{providerId}
    """)
    String findByProviderId(@Param("providerId") String providerId);


    @Update("""
        UPDATE user
        SET updated_at = #{updatedAt}
        WHERE provider = #{provider} AND provider_id = #{providerId}
""")
    int updateByUpdatedAt(@Param("provider") String provider,
                          @Param("providerId") String providerId,
                          @Param("updatedAt") ZonedDateTime updatedAt);

    @Insert("""
        INSERT INTO user (id, provider, provider_id, user_name_attribute, email, name, role, profile_image_url, created_at, updated_at)
        VALUES (#{id}, #{provider}, #{providerId}, #{userNameAttribute}, #{email}, #{name}, #{role}, #{profile_image_url}, #{createdAt}, #{updatedAt})
""")
    void save(User user);
}
