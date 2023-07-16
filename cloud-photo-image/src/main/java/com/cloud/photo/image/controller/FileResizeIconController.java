package com.cloud.photo.image.controller;

import com.cloud.photo.common.bo.FileResizeIconBo;
import com.cloud.photo.common.common.ResultBody;
import com.cloud.photo.common.util.RequestUtil;
import com.cloud.photo.image.service.IFileResizeIconService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 图片缩略图 前端控制器
 * </p>
 *
 * @author OUGE
 * @since 2023-07-14
 */
@RestController
@RequestMapping("/image")
@Slf4j
public class FileResizeIconController {

    @Autowired
    private IFileResizeIconService iFileResizeIconService;

    /**
     * 获取下载地址--通过资源池桶id，资源池objectid
     * @param request request
     * @param response response
     * @param fileResizeIconBo fileResizeIconBo
     * @return
     */
    @RequestMapping("/getIconUrl")
    public ResultBody getIconUrl(HttpServletRequest request, HttpServletResponse response,
                                 @RequestBody FileResizeIconBo fileResizeIconBo){
        String requestId = RequestUtil.getRequestId(request);
        RequestUtil.printQequestInfo(request);
        String url = iFileResizeIconService.getIconUrl(fileResizeIconBo.getUserId(),
                fileResizeIconBo.getFileId(), fileResizeIconBo.getIconCode());
        log.info("getPutUploadUrl() url = "+url );
        return ResultBody.success(url);
    }

}
