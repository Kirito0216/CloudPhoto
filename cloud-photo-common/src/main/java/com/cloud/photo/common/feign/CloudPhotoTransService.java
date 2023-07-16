package com.cloud.photo.common.feign;



import com.cloud.photo.common.bo.AlbumPageBo;
import com.cloud.photo.common.bo.FileUploadBo;
import com.cloud.photo.common.bo.StorageObjectBo;
import com.cloud.photo.common.bo.UserFileBo;
import com.cloud.photo.common.common.ResultBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Description:
 * tran服务接口
 * @author weifucheng
 * @date 2023/6/14 4:57 下午
 */
@FeignClient(value = "cloud-photo-trans",url = "http://127.0.0.1:9004")
@Service
public interface CloudPhotoTransService {

    /**
     * 获取下载地址
     * @param containerId
     * @param objectId
     * @return
     */
    @RequestMapping("/trans/getDownloadUrl")
    ResultBody getDownloadUrl(@RequestParam("containerId") String containerId , @RequestParam("objectId") String objectId);

    /**
     * 获取上传地址
     * @param userId
     * @param fileName
     * @return
     */
    @RequestMapping("/trans/getPutUploadUrl")
    ResultBody getPutUploadUrl(@RequestParam("userId") String userId,@RequestParam("filSize")  Long filSize,
                               @RequestParam("fileMd5")  String fileMd5,@RequestParam("fileName")  String fileName);

    /**
     * 获取上传地址
     * @param bo
     * @return
     */
    @RequestMapping("/trans/commit")
    public ResultBody commit(@RequestBody FileUploadBo bo);

    @RequestMapping("/trans/userFileList")
    ResultBody userFilelist(@RequestBody AlbumPageBo pageBo);

    @RequestMapping("/trans/getUserFileById")
    UserFileBo getUserFileById(@RequestParam("fileId") String fileId);

    @RequestMapping("/trans/getStorageObjectById")
    StorageObjectBo getStorageObjectById(@RequestParam("storageObjectId") String storageObjectId);

    @RequestMapping("/trans/updateUserFile")
    Boolean updateUserFile(@RequestBody List<UserFileBo> userFileBoList);
}
