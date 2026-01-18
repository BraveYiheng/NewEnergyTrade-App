package com.yiheng.springboot.control;

import com.yiheng.springboot.domain.User;
import com.yiheng.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/checkEmail")
    public ResponseEntity<Boolean> checkEmail(@RequestParam("email") String email) {
        boolean exists = userService.checkEmailExists(email);
        return ResponseEntity.ok(exists);
    }
    // 用户注册接口
    @PostMapping("/register")
    public boolean register(@RequestBody User user) {
        return userService.register(user);
    }

    // 用户登录接口
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User loginuser) {
        User user = userService.login(loginuser.getEmail(), loginuser.getPassword());
        Map<String, Object> responseMap = new HashMap<>();
        if (user!= null) {
            responseMap.put("success", true);
            responseMap.put("user", user);
        } else {
            responseMap.put("success", false);
            responseMap.put("message", "登录失败，请检查邮箱和密码是否正确");
        }
        System.out.println(responseMap);
        return responseMap;
    }

    // 根据用户ID获取用户信息接口
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable("userId") Integer userId) {
        return userService.getUserById(userId);
    }

    // 修改用户信息接口
    @PutMapping("/updateBasicInfo")
    public ResponseEntity<?> updateBasicInfo(@RequestBody User user) {
        boolean result = userService.updateBasicInfo(user);
        if (result) {
            System.out.println("个人基本信息更新成功");
            return new ResponseEntity<>("基本信息更新成功", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("基本信息更新失败", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // 更新密码接口
    @PutMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestBody User user) {
        boolean result = userService.updatePassword(user);
        if (result) {
            return new ResponseEntity<>("密码更新成功", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("密码更新失败", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
