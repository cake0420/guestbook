package com.cake7.guestbook.service;

import com.cake7.guestbook.domain.User;
import com.cake7.guestbook.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class.getName());

    @Override
    public void updateOrSaveUser(User user) throws Exception {
        try {
            int updatedRowCount = userMapper.updateByUpdatedAt(user.getProvider(), user.getProviderId(), user.getUpdatedAt());
            logger.debug("updatedRowCount: {}", updatedRowCount);
            if (updatedRowCount == 0) {
                userMapper.save(user);
                logger.debug("save user {} success", user);
            }
        } catch (Exception e) {
            logger.error("updateOrSaveUser error: {}", e.getMessage());
            throw new Exception("during updateOrSaveUser error: "+ e.getMessage());
        }
    }
}
