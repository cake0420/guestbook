package com.cake7.guestbook.mapper;

import com.cake7.guestbook.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserMapper {
    Optional<User> findByProviderAndProviderId(@Param("provider") String providerId, @Param("providerId") String providerName);
    void save(User user);
}
