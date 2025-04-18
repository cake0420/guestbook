package com.cake7.guestbook.service;

import com.cake7.guestbook.domain.User;
import com.cake7.guestbook.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    @Override
    public void updateOrSaveUser(User user) throws Exception {
        try {
            int updatedRowCount = userMapper.updateByUpdatedAt(user.getProvider(), user.getProviderId(), user.getUpdatedAt());
            if (updatedRowCount == 0) {
                userMapper.save(user);
            }
        } catch (Exception e) {
            throw new Exception("during updateOrSaveUser error "+ e.getMessage());
        }
    }
}
