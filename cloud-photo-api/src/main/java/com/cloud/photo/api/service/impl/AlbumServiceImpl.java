package com.cloud.photo.api.service.impl;


import com.cloud.photo.api.service.AlbumService;
import com.cloud.photo.common.bo.AlbumPageBo;
import com.cloud.photo.common.bo.FileAnalyzeBo;
import com.cloud.photo.common.bo.FileResizeIconBo;
import com.cloud.photo.common.common.ResultBody;
import com.cloud.photo.common.feign.CloudPhotoImageService;
import com.cloud.photo.common.feign.CloudPhotoTransService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author weifucheng
 */
@Service
public class AlbumServiceImpl implements AlbumService {

    @Autowired
    private CloudPhotoTransService cloudPhotoTransService;

    @Autowired
    private CloudPhotoImageService cloudPhotoImageService;

    @Override
    public ResultBody getUserAlbumList(AlbumPageBo pageBo) {
        return cloudPhotoTransService.userFilelist(pageBo);
    }

    @Override
    public ResultBody getUserAlbumDetail(FileAnalyzeBo analyzeBo) {
        return cloudPhotoImageService.getMediaInfo(analyzeBo);
    }

    @Override
    public ResultBody previewImage(FileResizeIconBo fileResizeIconBo) {
        return cloudPhotoImageService.getIconUrl(fileResizeIconBo);
    }
}
