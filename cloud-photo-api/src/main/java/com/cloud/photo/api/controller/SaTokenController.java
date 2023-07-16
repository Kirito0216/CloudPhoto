package com.cloud.photo.api.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.cloud.photo.api.common.CommonEnum;
import com.cloud.photo.api.common.ResultBody;
import com.cloud.photo.api.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SaTokenController {
    @Autowired
    private LoginService loginService;
    @GetMapping("/saTokenLogin")
    public ResultBody saTokenLogin(@RequestParam("phone") String phone){
        StpUtil.login(phone);
        final boolean login = StpUtil.isLogin();
        if (login){
            final String tokenValue = StpUtil.getTokenInfo().getTokenValue();
            return ResultBody.success(tokenValue);
        }
        return ResultBody.error(CommonEnum.LOGIN_FAIL);
    }

}
