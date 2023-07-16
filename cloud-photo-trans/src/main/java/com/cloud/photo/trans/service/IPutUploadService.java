package com.cloud.photo.trans.service;


import com.cloud.photo.common.bo.FileUploadBo;
import com.cloud.photo.trans.common.response.CommonEnum;
import com.cloud.photo.trans.common.response.ResultBody;
import org.springframework.stereotype.Service;

@Service
public interface IPutUploadService {

    String getPutUploadUrl(String fileName,String fileMd5,Long fileSize);

    CommonEnum commit(FileUploadBo bo);

    CommonEnum commitTransSecond(FileUploadBo bo);

}
