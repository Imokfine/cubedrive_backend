package com.imokfine.cubedrive.controller;

import com.imokfine.cubedrive.model.User;
import com.imokfine.cubedrive.service.UserService;
import com.imokfine.cubedrive.utils.JwtUtil;
import com.imokfine.cubedrive.utils.Result;
import com.imokfine.cubedrive.utils.ResultCodeEnum;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@CrossOrigin
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    JwtUtil jwtUtil;

    // 用户登录
    @PostMapping("login")
    public Result login(@RequestBody User user){
        Result result = userService.login(user);
        return result;
    }

    // 检查邮箱是否可用
    @PostMapping("checkEmail")
    public Result checkEmail(String email){
        Result result = userService.checkEmail(email);
        return result;
    }

    // 用户注册
    @PostMapping("register")
    public Result register(@RequestBody User user){
        Result result = userService.register(user);
        return result;
    }

    // 重置密码
    @PostMapping("resetPassword")
    public Result resetPassword(@RequestBody User user){
        Result result = userService.resetPassword(user);
        return result;
    }

    // 通过token获取用户数据
    @GetMapping("checkUserInfo")
    public Result checkUserInfo(@RequestHeader String token){
        Result result = userService.checkUserInfo(token);
        return result;
    }

    // 通过token检查登录状态
    @GetMapping("checkLogin")
    public Result checkLogin(@RequestHeader String token){
        boolean expiration = jwtUtil.isExpiration(token);
        if(expiration) return Result.build(null, ResultCodeEnum.NOTLOGIN);
        return Result.ok(null);
    }
}
