package com.cloud.photo.trans.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.photo.common.bo.FileUploadBo;
import com.cloud.photo.common.constant.CommonConstant;
import com.cloud.photo.trans.entity.StorageObject;
import com.cloud.photo.trans.entity.UserFile;
import com.cloud.photo.trans.mapper.StorageObjectMapper;
import com.cloud.photo.trans.mapper.UserFileMapper;
import com.cloud.photo.trans.service.IUserFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author OUGE
 * @since 2023-07-13
 */
@Service
public class UserFileServiceImpl extends ServiceImpl<UserFileMapper, UserFile> implements IUserFileService {

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;
    @Autowired
    private UserFileMapper userFileMapper;
    @Autowired
    private StorageObjectMapper storageObjectMapper;
    @Override
    public boolean saveAndFileDeal(FileUploadBo bo) {
        UserFile userFile =new UserFile();
        userFile.setUserId(bo.getUserId());
        userFile.setFileStatus(CommonConstant.FILE_STATUS_NORMA);//文件正常
        userFile.setCreateTime(LocalDateTime.now());
        userFile.setFileName(bo.getFileName());
        userFile.setIsFolder(CommonConstant.FILE_IS_FOLDER_NO);
        userFile.setAuditStatus(CommonConstant.FILE_AUDIT_ACCESS);
        userFile.setFileSize(bo.getFileSize());
        userFile.setModifyTime(LocalDateTime.now());
        userFile.setStorageObjectId(bo.getStorageObjectId());
        userFile.setCategory(bo.getCategory());
        userFile.setFileMd5(bo.getFileMd5());
        boolean result = this.save(userFile);

        //图片处理-格式分析
        kafkaTemplate.send(CommonConstant.FILE_AUDIT_TOPIC, JSONObject.toJSONString(userFile));//文件审核处理
        kafkaTemplate.send(CommonConstant.FILE_IMAGE_TOPIC, JSONObject.toJSONString(userFile));
        return result;
    }

    @Override
    public List<UserFile> getUserFileByFileName(String fileName) {
        return userFileMapper.selectList(new QueryWrapper<UserFile>().eq("file_name",fileName));
    }

    public static JSONObject jsonMerge(JSONObject source, JSONObject target) {
        // 覆盖目标JSON为空，直接返回覆盖源
        if (target == null) {
            return source;
        }
        for (String key : source.keySet()) {
            Object value = source.get(key);
            if (!target.containsKey(key)) {
                target.put(key, value);
            } else {
                if (value instanceof JSONObject) {
                    JSONObject valueJson = (JSONObject) value;
                    JSONObject targetValue = jsonMerge(valueJson, target.getJSONObject(key));
                    target.put(key, targetValue);
                } else if (value instanceof JSONArray) {
                    JSONArray valueArray = (JSONArray) value;
                    for (int i = 0; i < valueArray.size(); i++) {
                        JSONObject obj = (JSONObject) valueArray.get(i);
                        JSONObject targetValue = jsonMerge(obj, (JSONObject) target.getJSONArray(key).get(i));
                        target.getJSONArray(key).set(i, targetValue);
                    }
                } else {
                    target.put(key, value);
                }
            }
        }
        return target;
    }
}
