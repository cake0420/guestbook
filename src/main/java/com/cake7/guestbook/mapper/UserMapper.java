package com.cake7.guestbook.mapper;

import com.cake7.guestbook.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;

@Mapper
@Repository
public interface UserMapper {
    @Update("""
        UPDATE user
        SET updated_at = #{updatedAt}
        WHERE provider = #{provider} AND provider_id = #{providerId}
""")
    int updateByUpdatedAt(@Param("provider") String providerId,
                                     @Param("providerId") String providerName,
                                     @Param("updatedAt") ZonedDateTime updatedAt);

    @Insert("""
        INSERT INTO user (id, provider, provider_id, email, name, role, profile_image_url, created_at, updated_at)
        VALUES (#{id}, #{provider}, #{providerId}, #{email}, #{name}, #{role}, #{profile_image_url}, #{createdAt}, #{updatedAt})
""")
    void save(User user);
}
