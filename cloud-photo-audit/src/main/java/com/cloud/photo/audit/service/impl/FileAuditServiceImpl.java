package com.cloud.photo.audit.service.impl;

import com.cloud.photo.audit.entity.FileAudit;
import com.cloud.photo.audit.mapper.FileAuditMapper;
import com.cloud.photo.audit.service.IFileAuditService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.photo.common.bo.AuditPageBo;
import com.cloud.photo.common.bo.UserFileBo;
import com.cloud.photo.common.constant.CommonConstant;
import com.cloud.photo.common.feign.CloudPhotoTransService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 文件审核列表 服务实现类
 * </p>
 *
 * @author OUGE
 * @since 2023-07-14
 */
@Service
public class FileAuditServiceImpl extends ServiceImpl<FileAuditMapper, FileAudit> implements IFileAuditService {
    @Autowired
    IFileAuditService iFileAuditService;

    @Autowired
    CloudPhotoTransService cloudPhotoTransService;

    @Override
    public Boolean updateAuditStatus(AuditPageBo pageBo) {
        Integer auditStatus = pageBo.getAuditStatus();
        List<String> idsList = pageBo.getFileAuditIds();

        //读取审核存储信息
        List<UserFileBo> userFileBoList =new ArrayList<>();
        List<FileAudit> fileAuditList = iFileAuditService.listByIds(idsList);
        for(FileAudit fileAudit : fileAuditList){
            fileAudit.setAuditStatus(pageBo.getAuditStatus());

            UserFileBo userFileBo = new UserFileBo();
            userFileBo.setAuditStatus(auditStatus);
            userFileBo.setStorageObjectId(fileAudit.getStorageObjectId());
            userFileBoList.add(userFileBo);
        }

        //通过审核id更新人工审核列表
        Boolean updateResult = iFileAuditService.updateBatchById(fileAuditList);

        //审核不通过 通过存储id更新文件列表审核状态
        if(auditStatus.equals(CommonConstant.FILE_AUDIT_FAIL)){
            updateResult = cloudPhotoTransService.updateUserFile(userFileBoList);
        }

        return updateResult;
    }
}
