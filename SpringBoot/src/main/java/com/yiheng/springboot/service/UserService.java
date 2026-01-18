package com.yiheng.springboot.service;

import com.yiheng.springboot.domain.User;
import java.util.List;

public interface UserService {
    // 用户注册
    boolean register(User user);

    // 用户登录验证
    User login(String email, String password);

    // 根据用户ID获取用户信息
    User getUserById(Integer userId);

    //修改信息
    boolean updateBasicInfo(User user);
    boolean updatePassword(User user);

    boolean checkEmailExists(String email);
}
