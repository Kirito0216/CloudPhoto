package com.cloud.photo.trans.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.photo.common.bo.AlbumPageBo;
import com.cloud.photo.common.util.RequestUtil;
import com.cloud.photo.trans.common.response.ResultBody;
import com.cloud.photo.trans.entity.UserFile;
import com.cloud.photo.trans.service.IUserFileService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author OUGE
 * @since 2023-07-13
 */
@RestController
@RequestMapping("/trans")
public class UserFileController {
    @Resource
    private IUserFileService iUserFileService;
    //文件列表查询接口(分页查询文件)
    @RequestMapping("/userFileList")
    public ResultBody userFileList(HttpServletRequest request, HttpServletResponse response, @RequestBody AlbumPageBo pageBo){
        final String requestId = RequestUtil.getRequestId(request);
        RequestUtil.printQequestInfo(request);

        //1.设置查询mapper
        final QueryWrapper<UserFile> wrapper = new QueryWrapper<>();
        final HashMap<String, Object> param = new HashMap<>();
        if (pageBo.getCategory()!=null){
            param.put("category",pageBo.getCategory());
        }
        param.put("user_id",pageBo.getUserId());
        wrapper.allEq(param);
        Integer current = pageBo.getCurrent();
        Integer pageSize = pageBo.getPageSize();
        if (current==null){
            current = 1;
        }
        if (pageSize == null){
            pageSize = 20;
        }
        Page<UserFile> page = new Page<>(current, pageSize);
        Page<UserFile> userFilePage = iUserFileService.page(page, wrapper.orderByDesc("user_id", "create_time"));
        return ResultBody.success(userFilePage);
    }

    /**
     * 根据文件id获取文件信息
     * @param request request
     * @param response response
     * @param fileId fileId
     * @return UserFile
     */
    @RequestMapping("/getUserFileById")
    public UserFile getUserFileById(HttpServletRequest request, HttpServletResponse response,
                                    @RequestParam("fileId") String fileId){
        String requestId = RequestUtil.getRequestId(request);
        RequestUtil.printQequestInfo(request);

        return iUserFileService.getById(fileId);
    }

    /**
     * 根据文件名获取文件信息
     * @param request request
     * @param response response
     * @param fileName fileName
     * @return List<UserFile>
     */
    @RequestMapping("/getUserFileByFileName")
    public List<UserFile> getUserFileByFileName(HttpServletRequest request, HttpServletResponse response,
                                            @RequestParam("fileName") String fileName){
        final String requestId = RequestUtil.getRequestId(request);
        RequestUtil.printQequestInfo(request);

        return iUserFileService.getUserFileByFileName(fileName);
    }

    /**
     * 更新文件审核状态
     * @param request request
     * @param response response
     * @param userFileBoList userFileBoList
     * @return Boolean
     */
    @RequestMapping("/updateUserFile")
    public Boolean updateUserFile(HttpServletRequest request,HttpServletResponse response,
                                  @RequestBody List<UserFile> userFileBoList){
        final String requestId = RequestUtil.getRequestId(request);
        RequestUtil.printQequestInfo(request);

        for (UserFile userFile : userFileBoList) {
            final UpdateWrapper<UserFile> updateWrapper = new UpdateWrapper<>();

            //通过存储id更新审核状态
            if (StrUtil.isNotBlank(userFile.getStorageObjectId())){
                updateWrapper.eq("storage_object_id",userFile.getStorageObjectId());
            }
            updateWrapper.set("audit_status",userFile.getAuditStatus());
            iUserFileService.update(updateWrapper);
        }
        return true;
    }
}
