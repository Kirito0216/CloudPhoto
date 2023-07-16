package com.cloud.photo.api.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.cloud.photo.api.common.ResultBody;
import com.cloud.photo.api.feign.UserFeignService;
import com.cloud.photo.api.service.LoginService;
import com.cloud.photo.common.bo.UserBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class LoginController {
    @Autowired
    private UserFeignService userFeignService;
    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResultBody login(@RequestBody UserBo bo) {
        return loginService.login(bo);
    }

    @SaCheckLogin
    @PostMapping("/logout")
    public ResultBody logout() {
        return loginService.logout();
    }

    @SaCheckLogin
    @GetMapping("/getUserInfo")
    public ResultBody getUserInfo(){
        return loginService.getUserInfo();
    }
}
