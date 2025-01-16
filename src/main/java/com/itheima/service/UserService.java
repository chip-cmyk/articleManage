package com.itheima.service;

import com.itheima.pojo.User;
import org.hibernate.validator.constraints.URL;

public interface UserService {
    User findByUsername(String username);

    void register(String username, String password);

    void update(User user);

    void updateAvatar(@URL String avatarUrl);
}
