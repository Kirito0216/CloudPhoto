package com.cloud.photo.trans.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.photo.trans.entity.StorageObject;
import com.cloud.photo.trans.entity.UserFile;
import com.cloud.photo.trans.service.IDownLoadService;
import com.cloud.photo.trans.service.IStorageObjectService;
import com.cloud.photo.trans.service.IUserFileService;
import com.cloud.photo.trans.util.S3Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class DownLoadServiceImpl implements IDownLoadService {
    @Resource
    private IUserFileService iUserFileService;
    @Autowired
    private IStorageObjectService iStorageObjectService;

    @Override
    public String getDownLoadUrlByFileId(String userId, String fileId) {
        //查询文件信息
        UserFile userFile = iUserFileService.getById(fileId);
        if (userFile == null){
            log.error("getDownLoadUrlByFileId() userFile is null, fileId = "+fileId);
            return null;
        }
        //查询文件存储信息
        StorageObject storageObject = iStorageObjectService.getById(userFile.getStorageObjectId());
        if (storageObject == null){
            log.error("getDownLoadUrlByFileId() storageObject is null , fileId = "+fileId);
            return null;
        }

        //生成地址
        return S3Util.getDownloadUrl(storageObject.getContainerId(),storageObject.getObjectId(),userFile.getFileName());
    }

    @Override
    public String getDownLoadUrl(String containerId, String objectId) {
        return S3Util.getDownloadUrl(containerId,objectId);
    }
}
