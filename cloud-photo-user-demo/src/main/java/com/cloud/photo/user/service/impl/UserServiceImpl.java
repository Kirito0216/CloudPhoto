package com.cloud.photo.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.photo.user.common.response.CommonEnum;
import com.cloud.photo.user.common.response.ResultBody;
import com.cloud.photo.user.entity.User;
import com.cloud.photo.user.mapper.UserMapper;
import com.cloud.photo.user.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author OUGE
 * @since 2023-07-12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Resource
    private IUserService userService;
    @Resource
    private UserMapper userMapper;

    public ResultBody getUserByPhoneNumber(String pNumber){
        final QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("phone",pNumber);
        final User user = userService.getOne(wrapper);
        if (user==null){
            return ResultBody.error(CommonEnum.USER_IS_NULL);
        }
        return ResultBody.success(user);
    }

    public ResultBody getUserById(String id){
        final QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",id);
        final User user = userService.getOne(wrapper);
        return user==null ? ResultBody.error(CommonEnum.USER_IS_NULL) : ResultBody.success(user);
    }
}

