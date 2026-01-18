package com.yiheng.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yiheng.springboot.mapper.UserMapper;
import com.yiheng.springboot.domain.User;
import com.yiheng.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserMapper userMapper;
    @Override
    public boolean register(User user) {
        return userMapper.insert(user) > 0;
    }
    @Override
    public User getUserById(Integer userId) {
        return userMapper.selectById(userId);
    }
    @Override
    public boolean updateBasicInfo(User user) {
        return userMapper.updateById(user) > 0;
    }
    @Override
    public boolean updatePassword(User user) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", user.getUserId());
        updateWrapper.set("password", user.getPassword());
        return userMapper.update(user, updateWrapper) > 0;
    }

    @Override
    public boolean checkEmailExists(String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        return userMapper.selectOne(queryWrapper)!= null;
    }

    @Override
    public User login(String email, String password) {
        System.out.println(email);
        // 根据邮箱查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        User user = userMapper.selectOne(queryWrapper);
        if (user!= null) {
            System.out.println("根据邮箱找到用户");
            if (password.equals(user.getPassword())) {
                System.out.println("登陆成功");
                return user;
            }
            else{ System.out.println("密码错误");
                return null;
            }
        }
        System.out.println("根据邮箱找不到用户");
        return null;
    }




}