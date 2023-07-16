package com.cloud.photo.trans.service;

import org.springframework.stereotype.Service;

public interface IDownLoadService {
    String getDownLoadUrlByFileId(String userId,String fileId);
    String getDownLoadUrl(String containerId,String objectId);
}
