package com.cloud.photo.trans.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.photo.common.bo.AlbumPageBo;
import com.cloud.photo.common.util.RequestUtil;
import com.cloud.photo.trans.common.response.ResultBody;
import com.cloud.photo.trans.entity.StorageObject;
import com.cloud.photo.trans.entity.UserFile;
import com.cloud.photo.trans.service.IDownLoadService;
import com.cloud.photo.trans.service.IStorageObjectService;
import com.cloud.photo.trans.service.IUserFileService;
import com.cloud.photo.trans.util.S3Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Objects;

@RestController
@RequestMapping("/trans")
@Slf4j
public class DownLoadController {
    @Autowired
    private IDownLoadService iDownLoadService;

    /**
     * 获取下载地址--通过文件id
     * @param request  request
     * @param response response
     * @param userId userId
     * @param fileId fileId
     * @return ResultBody
     */
    @RequestMapping("/getDownLoadUrlByFileId")
    public ResultBody getDownLoadUrlByFileId(HttpServletRequest request, HttpServletResponse response,
                                             @RequestParam String userId,@RequestParam String fileId){
        String requestId = RequestUtil.getRequestId(request);
        RequestUtil.printQequestInfo(request);
        String url = iDownLoadService.getDownLoadUrlByFileId(userId,fileId);
        log.info("getPutUploadUrl() userId = " + userId + " , url = "+ url);
        return ResultBody.success(url);
    }

    /**
     * 获取下载地址--通过资源池桶id，资源池objectid
     * @param request request
     * @param response response
     * @param containerId containerId
     * @param objectId objectId
     * @return ResultBody
     */
    @RequestMapping("/getDownloadUrl")
    public ResultBody getDownloadUrl(HttpServletRequest request, HttpServletResponse response,
                                     @RequestParam String containerId ,@RequestParam String objectId){
        String requestId = RequestUtil.getRequestId(request);
        RequestUtil.printQequestInfo(request);
        String url = iDownLoadService.getDownLoadUrl(containerId,objectId);
        log.info("getPutUploadUrl() url = "+url );
        return ResultBody.success(url);
    }


}
