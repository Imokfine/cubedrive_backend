package com.imokfine.cubedrive.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imokfine.cubedrive.model.User;
import com.imokfine.cubedrive.service.UserService;
import com.imokfine.cubedrive.mapper.UserMapper;
import com.imokfine.cubedrive.utils.JwtUtil;
import com.imokfine.cubedrive.utils.MD5Util;
import com.imokfine.cubedrive.utils.Result;
import com.imokfine.cubedrive.utils.ResultCodeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
* @author ponponpon
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-10-27 23:52:04
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;

    @Resource
    private JwtUtil jwtUtil;

    /**
     * 通过邮箱查找用户
     * @param user
     * @return
     */
    public User findUser(User user){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getEmail, user.getEmail());
        return getOne(lambdaQueryWrapper); // 存在返回User对象，不存在返回null
    }

    /**
     * 用户登录
     * @param user
     * @return
     */
    @Override
    public Result login(User user) {

        // 在数据库中查找用户
        User loginUser = findUser(user);

        // 如果用户不存在
        if(loginUser == null) return Result.build(null,ResultCodeEnum.EMAIL_ERROR);

        // 密码不为空，对比密码
        if(!StringUtils.isEmpty(user.getPassword())
                && MD5Util.encrypt(user.getPassword()).equals(loginUser.getPassword())){

            // 生成token
            String token = jwtUtil.createToken(Long.valueOf(loginUser.getUserId()));

            // 返回带token的Result
            Map data = new HashMap();
            data.put("token",token);

            return Result.ok(data);
        }

        // 密码有误
        return Result.build(null,ResultCodeEnum.PASSWORD_ERROR);
    }

    /**
     * 检查邮箱是否可用
     * @param email
     * @return
     */
    @Override
    public Result checkEmail(String email) {

        // 在数据库中查找邮箱
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getEmail, email);
        int count = userMapper.selectCount(lambdaQueryWrapper).intValue();

        // 如果邮箱不存在
        if(count == 0) return Result.ok(null);

        // 如果邮箱存在
        return Result.build(null,ResultCodeEnum.EMAIL_USED);
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @Override
    public Result register(User user) {

        // 在数据库中查找邮箱
        Result result = checkEmail(user.getEmail());

        // 如果邮箱存在
        if(result.getCode() == 505) return result;

        // 如果注册信息有误
        if(StringUtils.isEmpty(user.getEmail())
                || StringUtils.isEmpty(user.getPassword())
                || StringUtils.isEmpty(user.getUsername())){
            return Result.build(null, ResultCodeEnum.REGISTER_ERROR);
        }

        // 注册成功，设置MD5密码，加入数据库
        user.setPassword(MD5Util.encrypt(user.getPassword()));
        user.setRegisterTime(new Date());
        userMapper.insert(user);

        return Result.ok(null);
    }

    /**
     * 用户重设密码
     * @param user
     * @return
     */
    @Override
    public Result resetPassword(User user) {

        // 在数据库中查找用户
        User loginUser = findUser(user);

        // 如果用户不存在
        if(loginUser == null) return Result.build(null,ResultCodeEnum.EMAIL_ERROR);

        // 用户存在
        user.setPassword(MD5Util.encrypt(user.getPassword()));
        userMapper.updateById(user);

        return Result.ok(null);
    }

    /**
     * 根据token查看用户信息
     * @param token
     * @return
     */
    @Override
    public Result checkUserInfo(String token) {

        // 检查token是否过期，true为已过期
        boolean expiration = jwtUtil.isExpiration(token);

        // 如果token过期
        if(expiration) return Result.build(null,ResultCodeEnum.NOTLOGIN);

        // token没过期，去掉密码返回用户信息
        int userId = jwtUtil.getUserId(token).intValue();
        User user = userMapper.selectById(userId);
        user.setPassword("");

        Map data = new HashMap();
        data.put("loginUser", user);

        return Result.ok(data);
    }
}




