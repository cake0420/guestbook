package com.cake7.guestbook.service;

import com.cake7.guestbook.domain.User;

public interface UserService {
    void updateOrSaveUser(User user) throws Exception;
}
