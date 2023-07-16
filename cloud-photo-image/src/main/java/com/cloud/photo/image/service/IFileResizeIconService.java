package com.cloud.photo.image.service;

import com.cloud.photo.image.entity.FileResizeIcon;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 图片缩略图 服务类
 * </p>
 *
 * @author OUGE
 * @since 2023-07-14
 */
public interface IFileResizeIconService extends IService<FileResizeIcon> {
    String getIconUrl(String userId, String fileId, String iconCode);

    void imageThumbnailAndMediaInfo(String storageObjectId, String fileName);
}
