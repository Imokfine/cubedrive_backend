package com.imokfine.cubedrive;

import com.imokfine.cubedrive.mapper.UserMapper;
import com.imokfine.cubedrive.model.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
class CubedriveApplicationTests {

    @Test
    void contextLoads() {
    }


    @Resource
    private UserMapper userMapper;

    @Test
    void findAll(){
        List<User> users = userMapper.selectList(null);
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    void test01(){
        User user = new User();

        user.setUsername("qwert");
        user.setPassword("123456");
        user.setEmail("asdf@163.com");
        user.setRegisterTime(new Date());
        userMapper.insert(user);
    }

    @Test
    void test02(){
        User user = userMapper.selectById(1);
        System.out.println(user.getUserId());

    }
}
