package com.cloud.photo.trans.controller;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.cloud.photo.common.bo.FileUploadBo;
import com.cloud.photo.trans.common.response.CommonEnum;
import com.cloud.photo.trans.common.response.ResultBody;
import com.cloud.photo.trans.service.IPutUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.cloud.photo.common.util.RequestUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/trans")
@Slf4j
public class PutUploadController {

    @Resource
    private IPutUploadService iPutUploadService;

    @RequestMapping("/getPutUploadUrl")
    public ResultBody getPutUploadUrl(HttpServletRequest request, HttpServletResponse response,
                                      @RequestParam(value = "userId", required = false) String userId,
                                      @RequestParam(value = "filSize",required = false) Long fileSize,
                                      @RequestParam(value ="fileMd5",required = false) String fileMd5,
                                      @RequestParam(value ="fileName") String fileName) {
        Long startTime = System.currentTimeMillis();
        final String requestId = RequestUtil.getRequestId(request);
        RequestUtil.printQequestInfo(request);
        String result = iPutUploadService.getPutUploadUrl(fileName, fileMd5, fileSize);
        Long endTime = System.currentTimeMillis();
        log.info("getPutUploadUrl() userId = " + userId + " , result = " + result +", cost = " +(endTime-startTime) +"ms");
        return ResultBody.success(result,requestId);
    }

    @RequestMapping("/commit")
    public ResultBody commit(HttpServletRequest request, HttpServletResponse response,@RequestBody FileUploadBo bo) {
        //打印请求日志
        String requestId = RequestUtil.getRequestId(request);
        RequestUtil.printQequestInfo(request);
        //返回值
        CommonEnum result;
        //判断文件是否秒传
        if (StringUtils.isBlank(bo.getStorageObjectId())) {
            //处理非秘传
            result = iPutUploadService.commit(bo);
        } else {
            //秒传
            result = iPutUploadService.commitTransSecond(bo);
        }
        log.info("getPutUploadUrl() userId = " + bo.getUserId() + ", result = " + result);
        if (StringUtils.equals(result.getResultMsg(), CommonEnum.SUCCESS.getResultMsg())){
            return ResultBody.success(CommonEnum.SUCCESS.getResultMsg(),requestId);
        }else {
            return ResultBody.error(result.getResultCode(),result.getResultMsg(),requestId);
        }
    }
}
