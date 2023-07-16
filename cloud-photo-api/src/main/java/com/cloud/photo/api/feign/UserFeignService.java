package com.cloud.photo.api.feign;


import com.cloud.photo.api.common.ResultBody;
import com.cloud.photo.common.bo.UserBo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cloud-photo-user",url = "http://127.0.0.1:9007")
@Service
public interface UserFeignService {
    @GetMapping("/user/getUserInfo")
    ResultBody getUserInfo(@RequestParam("phone") String phone);

    /**
     * 执行用户登录
     * @param bo 用户登录信息
     * @return 登录结果
     */
    @PostMapping("/user/login")
    ResultBody login(UserBo bo);

    /**
     * 查询用户是否存在
     * @param phone 手机号
     * @return 查询结果
     */
    @GetMapping("/user/checkPhone")
    ResultBody checkPhone(@RequestParam(value = "phone") String phone);

    /**
     * 检查账密
     * @param userName 账号
     * @param password 密码
     * @return 查询结果
     */
    @GetMapping("/user/checkAdmin")
    ResultBody checkAdmin(@RequestParam(value = "userName") String userName, @RequestParam(value = "password") String password);
}
