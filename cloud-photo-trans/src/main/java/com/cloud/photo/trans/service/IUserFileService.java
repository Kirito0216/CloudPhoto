package com.cloud.photo.trans.service;

import com.cloud.photo.common.bo.FileUploadBo;
import com.cloud.photo.trans.entity.UserFile;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author OUGE
 * @since 2023-07-13
 */
public interface IUserFileService extends IService<UserFile> {
    boolean saveAndFileDeal(FileUploadBo bo);
    List<UserFile> getUserFileByFileName(String fileName);
}
