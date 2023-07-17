package com.cloud.photo.user.mapper;

/*import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.photo.user.entity.User;
import com.cloud.photo.user.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.cloud.photo.user.mapper.UserMapper;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserServiceImpl userService;
    @Test
    public void test01(){
        System.out.println(userMapper);
        final List<User> users = userMapper.selectList(null);
        System.out.println("users = " + users);
    }

    @Test
    public void test02(){
        final User user = new User();
        user.setUserId("4");
        user.setUserName("jerry");
        final boolean res = userService.save(user);
        System.out.println("res = " + res);
    }

    @Test
    public void test03(){
        final User user = new User();
        user.setUserName("tom");
        user.setUserId("3");
        final QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",user.getUserId());
        final int update = userMapper.update(user, wrapper);
        System.out.println("update = " + update);
    }
}*/
