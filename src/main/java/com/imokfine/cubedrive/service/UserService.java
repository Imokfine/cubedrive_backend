package com.imokfine.cubedrive.service;

import com.imokfine.cubedrive.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.imokfine.cubedrive.utils.Result;

/**
* @author ponponpon
* @description 针对表【user】的数据库操作Service
* @createDate 2023-10-27 23:52:04
*/
public interface UserService extends IService<User> {

    Result login(User user);

    Result checkEmail(String email);

    Result register(User user);

    Result resetPassword(User user);

    Result checkUserInfo(String token);
}
