package com.itheima.controller;

import com.itheima.pojo.Result;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import com.itheima.utils.JwtUtil;
import com.itheima.utils.Md5Util;
import com.itheima.utils.ThreadLocalUtil;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.ui.freemarker.SpringTemplateLoader;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$", message = "用户名长度需要5-16位") String username, @Pattern(regexp = "^\\S{5,16}$", message = "密码长度需要5-16位") String password) {
        User user = userService.findByUsername(username);
        if (user == null) {
            userService.register(username, password);
            return Result.success();
        } else {
            return Result.error("用户名已被占用");
        }
    }

    @PostMapping("/login")
    public Result login(@Pattern(regexp = "^\\S{5,16}$", message = "用户名长度需要5-16位") String username, @Pattern(regexp = "^\\S{5,16}$", message = "密码长度需要5-16位") String password) {
        User loginUser = userService.findByUsername(username);
        if (loginUser == null) {
            return Result.error("用户名错误");
        }
        if (Md5Util.getMD5String(password).equals(loginUser.getPassword())) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginUser.getId());
            claims.put("username", loginUser.getUsername());
            String token = JwtUtil.genToken(claims);

            stringRedisTemplate.opsForValue().set(String.valueOf(loginUser.getId()), token, JwtUtil.EXPIRE_TIME, TimeUnit.MILLISECONDS);
            return Result.success(token);
        }

        return Result.error("密码错误");
    }


    @GetMapping("/userInfo")
    public Result<User> userInfo() {
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = map.get("username").toString();
        User user = userService.findByUsername(username);
        return Result.success(user);
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Validated User user) {
        userService.update(user);
        return Result.success();
    }

    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam("avatarUrl") @URL String avatarUrl) {
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody @Validated Map<String,
            @Pattern(regexp = "^\\S{5,16}$", message = "密码长度需要 5-16 位，且不能包含空格") String> params, @RequestHeader("Authorization") Spring token) {
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");

        Map<String, Object> map = ThreadLocalUtil.get();
        String username = map.get("username").toString();
        User user = userService.findByUsername(username);
        if (!user.getPassword().equals(Md5Util.getMD5String(oldPwd))) {
            return Result.error("原密码填写不正确");
        }

        if (!rePwd.equals(newPwd)) {
            return Result.error("两次填写的密码不一致");
        }

        userService.updatePwd(newPwd);
        stringRedisTemplate.opsForValue().getOperations().delete(String.valueOf(user.getId()));
        return Result.success();
    }
}
