package com.cake7.guestbook.mapper;

import com.cake7.guestbook.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Mapper
@Repository
public interface UserMapper {
    @Select("""
            SELECT *
            FROM user
            WHERE provider = #{provider} AND provider_id = #{providerId}
""")
    Optional<User> findByProviderAndProviderId(@Param("provider") String providerId, @Param("providerId") String providerName);

    @Insert("""
        INSERT INTO user (id, provider, provider_id, email, name, role, profile_image_url, created_at, updated_at)
        VALUES (#{id}, #{provider}, #{providerId}, #{email}, #{name}, #{role}, #{profile_image_url}, #{createdAt}, #{updatedAt})
""")
    void save(User user);
}
