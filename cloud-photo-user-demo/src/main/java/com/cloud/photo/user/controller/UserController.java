package com.cloud.photo.user.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.photo.common.bo.UserBo;
import com.cloud.photo.common.constant.CommonConstant;
import com.cloud.photo.user.common.response.CommonEnum;
import com.cloud.photo.user.common.response.ResultBody;
import com.cloud.photo.user.entity.User;
import com.cloud.photo.user.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author OUGE
 * @since 2023-07-12
 */
@RestController
@Slf4j
//@RefreshScope(proxyMode = ScopedProxyMode.DEFAULT) //实现nacos热更新
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/user/test/{id}")
    public String test(@PathVariable("id") Integer id){
        return String.valueOf(id);
    }

    @GetMapping("/user/getUserInfo")
    public ResultBody getUserInfo(@RequestParam(value = "phone") String phone) {
        log.info("getUserInfo()-phone=" + phone + ",start!");
        //从user表里面获取一条记录，phone=传进来的phone
        //new QueryWrapper<User>().eq("phone", phone)
        User user = userService.getOne(new QueryWrapper<User>().eq("phone", phone));
        //打印拿到的信息
        log.info("getUserInfo()-phone=" + phone + ",user=" + user);

        //1、json形式的数据
        //2、自定义业务的结构体

        //ResultBody.error(CommonEnum.USER_IS_NULL)

        ResultBody resultBody = (user == null ? ResultBody.error(CommonEnum.USER_IS_NULL) : ResultBody.success(user));
        log.info("getUserInfo()-phone=" + phone + ",resultBody=" + resultBody);
        return resultBody;
    }

@GetMapping("/user/getUserById")
    public ResultBody getUserById(@RequestParam String id){
        return userService.getUserById(id);
    }

    /**
     * 查询用户是否存在
     *
     * @param phone 手机号
     * @return 查询结果
     */
    @GetMapping("/user/checkPhone")
    public ResultBody checkPhone(@RequestParam(value = "phone") String phone) {
        User userEntity = userService.getOne(new QueryWrapper<User>().eq("phone", phone));
        return userEntity == null ? ResultBody.error(CommonEnum.USER_IS_NULL) : ResultBody.success();
    }

    /**
     * 检查账密
     * @param userName 账号
     * @param password 密码
     * @return 查询结果
     */
    @GetMapping("/user/checkAdmin")
    public ResultBody checkAdmin(@RequestParam(value = "userName") String userName, @RequestParam(value = "password") String password){
        User admin = userService.getOne(new QueryWrapper<User>()
                .eq("user_name", userName)
                .eq("password", password)
                .eq("role", CommonConstant.ADMIN));
        return admin == null ? ResultBody.error(CommonEnum.USERNAME_PASSWORD_ERROR) : ResultBody.success();
    }

    /**
     * 执行用户登录
     *
     * @param bo 存放用户信息的实体
     * @return 登录结果
     */
    @PostMapping("/user/login")
    public ResultBody login(@RequestBody UserBo bo) {
        String phone = bo.getPhone();
        //看看有没有这个用户
        User user = userService.getOne(new QueryWrapper<User>().eq("phone", phone));
        if (user == null) {
            //没有就新增这个用户信息
            user = new User();
            //复制
            BeanUtils.copyProperties(bo, user);
            //自定义一个用户ID
            user.setUserId(RandomUtil.randomString(9));
            user.setCreateTime(DateUtil.date());
            user.setUpdateTime(DateUtil.date());
            user.setLoginCount(0);
            //普通用户
            user.setRole("user");

        } else {
            //有这个用户就更新下登录信息
            user.setLoginCount(user.getLoginCount() + 1);
            user.setUpdateTime(DateUtil.date());
        }
        //更新信息入库
        boolean saveOrUpdate = userService.saveOrUpdate(user);
        return saveOrUpdate ? ResultBody.success() : ResultBody.error(CommonEnum.LOGIN_FAIL);
    }
}

