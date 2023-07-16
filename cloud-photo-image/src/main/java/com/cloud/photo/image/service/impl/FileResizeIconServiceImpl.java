package com.cloud.photo.image.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.word.PicType;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.packagescan.util.ResourceUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.photo.common.bo.StorageObjectBo;
import com.cloud.photo.common.bo.UserFileBo;
import com.cloud.photo.common.common.ResultBody;
import com.cloud.photo.common.constant.CommonConstant;
import com.cloud.photo.common.feign.CloudPhotoTransService;
import com.cloud.photo.image.entity.FileResizeIcon;
import com.cloud.photo.image.entity.MediaInfo;
import com.cloud.photo.image.mapper.FileResizeIconMapper;
import com.cloud.photo.image.service.IFileResizeIconService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.photo.image.service.IMediaInfoService;
import com.cloud.photo.image.util.DownloadFileUtil;
import com.cloud.photo.image.util.PicUtils;
import com.cloud.photo.image.util.UploadFileUtil;
import com.cloud.photo.image.util.VipsUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.UUID;

/**
 * <p>
 * 图片缩略图 服务实现类
 * </p>
 *
 * @author OUGE
 * @since 2023-07-14
 */
@Service
public class FileResizeIconServiceImpl extends ServiceImpl<FileResizeIconMapper, FileResizeIcon> implements IFileResizeIconService {
    @Autowired
    private CloudPhotoTransService cloudPhotoTransService;

    @Autowired
    private IMediaInfoService iMediaInfoService;

    @Override
    public String getIconUrl(String userId, String fileId, String iconCode) {
        //查询文件信息
        UserFileBo userFile = cloudPhotoTransService.getUserFileById(fileId);
        String storageObjectId = userFile.getStorageObjectId();
        String fileName = userFile .getFileName();
        String suffixName = fileName.substring(fileName.lastIndexOf(",")+1,fileName.length());

        //存储文件存储信息
        final StorageObjectBo storageObject = cloudPhotoTransService.getStorageObjectById(userFile.getStorageObjectId());
        //查询缩略图信息
        final FileResizeIcon fileResizeIcon = getFileResizeIcon(userFile.getStorageObjectId(), iconCode);
        String objectId = null;
        String containerId = null;
        //缩略图不存在   生成缩略图
        if (fileResizeIcon == null){
            /*String srcFileName = downloadImage(storageObject.getContainerId(), storageObject.getObjectId(), suffixName);
            this.imageThumbnailSave(iconCode,suffixName,srcFileName,storageObjectId,fileName);*/
            imageThumbnailAndMediaInfo(storageObjectId,fileName);
        }else {
            objectId = fileResizeIcon.getObjectId();
            containerId = fileResizeIcon.getContainerId();
        }

        //生成缩略图下载地址
        final ResultBody iconUrlResponse = cloudPhotoTransService.getDownloadUrl(containerId, objectId);
        return iconUrlResponse.getData().toString();
    }

    //图片处理&图片格式分析
    @Override
    public void imageThumbnailAndMediaInfo(String storageObjectId, String fileName) {
        String iconCode200 = "200_200";
        String iconCode600 = "600_600";

        //查询尺寸200和尺寸600缩略图 是否存在  - 同一张缩略图无需重复生成
        FileResizeIcon fileResizeIcon200 = getFileResizeIcon(storageObjectId,iconCode200);
        FileResizeIcon fileResizeIcon600 = getFileResizeIcon(storageObjectId,iconCode600);

        //查询图片是否分析属性
        MediaInfo mediaInfo = iMediaInfoService.getOne(new QueryWrapper<MediaInfo>().eq("storage_Object_Id", storageObjectId) ,false);

        //缩略图已存在&图片已分析
        if(fileResizeIcon200!=null && fileResizeIcon600 !=null && mediaInfo!=null){
            return ;
        }

        //缩略图不存在-下载原图
        String suffixName = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
        StorageObjectBo storageObject = cloudPhotoTransService.getStorageObjectById(storageObjectId);
        String srcFileName = downloadImage(storageObject.getContainerId(), storageObject.getObjectId(), suffixName);

        //原图下载失败
        if(StringUtils.isBlank(srcFileName)){
            log.error("downloadResult error!");
            return;
        }

        //生成缩略图 保存入库
        if (fileResizeIcon200 == null){
            this.imageThumbnailSave(iconCode200,suffixName,srcFileName,storageObjectId,fileName);
        }
        if (fileResizeIcon600 == null){
            this.imageThumbnailSave(iconCode600,suffixName,srcFileName,storageObjectId,fileName);
        }
        //图片格式分析&入库
        MediaInfo newMediaInfo = PicUtils.analyzePicture(new File(srcFileName));
        newMediaInfo.setStorageObjectId(storageObjectId);
        if (StringUtils.isBlank(newMediaInfo.getShootingTime())){
            newMediaInfo.setShootingTime(DateUtil.now());
        }
        iMediaInfoService.save(newMediaInfo);
    }

    /**
     * 查询缩略图是否存在
     * @param storageObjectId storageObjectId
     * @param iconCode iconCode
     * @return FileResizeIcon
     */
    public FileResizeIcon getFileResizeIcon(String storageObjectId, String iconCode) {

        //1.设置查询Mapper
        QueryWrapper<FileResizeIcon> qw = new QueryWrapper<>();
        //2.组装查询条件
        HashMap<String, Object> param = new HashMap<>();
        param.put("storage_object_id",storageObjectId);
        param.put("icon_code",iconCode);
        qw.allEq(param);
        return this.getOne(qw,false);
    }

    /**
     * 下载原图
     * @param containerId containerId
     * @param objectId objectId
     * @param suffixName suffixName
     * @return String
     */
    private String downloadImage(String containerId, String objectId, String suffixName) {
        //获取下载地址
        String srcFileDirName = "/data/edrive/tmp/";
        ResultBody baseResponse = cloudPhotoTransService.getDownloadUrl(containerId,objectId);
        String url = baseResponse.getData().toString();

        String srcFileName  =  srcFileDirName + UUID.randomUUID().toString() +"." +suffixName;
        File dir = new File(srcFileDirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        Boolean downloadResult = DownloadFileUtil.downloadFile(url, srcFileName);
        if(!downloadResult){
            return null;
        }
        return srcFileName;
    }


    /**
     * 原图生成缩略图
     * @param iconCode iconCode
     * @param suffixName suffixName
     * @param srcFileName srcFileName
     * @param storageObjectId storageObjectId
     * @param fileName fileName
     * @return FileResizeIcon
     */
    private FileResizeIcon imageThumbnailSave(String iconCode,String suffixName,String srcFileName,
                                              String storageObjectId,String fileName) {

        //文件路径
        String srcFileDirName = "/data/edrive/tmp/";

        //生成缩略图
        String iconFileName  =  srcFileDirName + UUID.randomUUID().toString()+"." + suffixName;
        int width = Integer.parseInt(iconCode.split("_")[0]);
        int height = Integer.parseInt(iconCode.split("_")[1]);
        VipsUtil.thumbnail(srcFileName,iconFileName,width,height,"70");

        //文件为空或者截图失败
        if(StringUtils.isBlank(iconFileName) || !new File(iconFileName).exists()){
            return null;
        }

        //上传缩略图 & 入库
        FileResizeIcon fileResizeIcon = this.uploadIcon(null,storageObjectId ,iconCode, new File(iconFileName),fileName);
        return fileResizeIcon;
    }


    private FileResizeIcon uploadIcon(String userId,String storageObjectId ,String iconCode, File iconFile,String fileName) {
        //上传缩略图
        ResultBody uploadUrlResponse = cloudPhotoTransService.getPutUploadUrl(userId,null,null,fileName);
        JSONObject jsonObject = JSONObject.parseObject(uploadUrlResponse.getData().toString());
        String objectId = jsonObject.getString("objectId");
        String uploadUrl = jsonObject.getString("url");
        String containerId= jsonObject.getString("containerId");

        //上传文件到存储池
        UploadFileUtil.uploadSinglePart(iconFile,uploadUrl);

        //保存入库
        FileResizeIcon newFileResizeIcon = new FileResizeIcon(storageObjectId ,iconCode ,containerId,objectId);
        this.save(newFileResizeIcon);
        return newFileResizeIcon;
    }


    public String getAuditFailIconUrl() {

        //查询默认图是否存在存储池  不存在 上传到存储池
        String iconStorageObjectId = CommonConstant.ICON_STORAGE_OBJECT_ID;
        StorageObjectBo iconStorageObject = cloudPhotoTransService.getStorageObjectById(iconStorageObjectId);
        String containerId = "";
        String objectId = "";
        String srcFileName = "";
        if(iconStorageObject == null){
            File file = null;
            try {
                file = ResourceUtils.getFile("classpath:static/auditFail.jpg");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            FileResizeIcon newFileResizeIcon =  this.uploadIcon(null,iconStorageObjectId ,"200_200", file,"auditFail.jpg");
            containerId = newFileResizeIcon.getContainerId();
            objectId = newFileResizeIcon.getObjectId();
        }else{
            containerId = iconStorageObject.getContainerId();
            objectId = iconStorageObject.getObjectId();
        }
        //生成缩略图下载地址
        ResultBody iconUrlResponse = cloudPhotoTransService.getDownloadUrl(containerId,objectId);
        return iconUrlResponse.getData().toString();
    }
}
