package com.cloud.photo.audit.consumer;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.photo.audit.entity.FileAudit;
import com.cloud.photo.audit.service.IFileAuditService;
import com.cloud.photo.common.bo.UserFileBo;
import com.cloud.photo.common.constant.CommonConstant;
import com.cloud.photo.common.feign.CloudPhotoTransService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuditConsumer {

    @Autowired
    IFileAuditService iFileAuditService;
    @Autowired
    CloudPhotoTransService cloudPhotoTransService;

    // 消费监听
    @KafkaListener(topics = {"file_audit_topic"})
    public void onMessage(ConsumerRecord<String, Object> record){
        // 消费的哪个topic、partition的消息,打印出消息内容
        System.out.println("消费："+record.topic()+"-"+record.partition()+"-"+record.value());
        Object value = record.value();
        JSONObject jsonObject = JSONObject.parseObject(value.toString());
        String userFileId = jsonObject.getString("userFileId");
        String fileMd5 = jsonObject.getString("fileMd5");
        String fileName = jsonObject.getString("fileName");
        Integer fileSize = jsonObject.getInteger("fileSize");
        String storageObjectId = jsonObject.getString("storageObjectId");

        //根据文件MD5读取审核状态  相同文件只需要审核一次
        FileAudit fileAudit = iFileAuditService.getOne(new QueryWrapper<FileAudit>().eq("md5", fileMd5), false);

        //未人工审核  加入审核列表
        if(fileAudit == null){
            //未审核  插入审核列表
            FileAudit audit = new FileAudit();
            audit.setFileName(fileName);
            audit.setMd5(fileMd5);
            audit.setFileSize(fileSize);
            audit.setStorageObjectId(storageObjectId);
            audit.setAuditStatus(CommonConstant.FILE_AUDIT);
            audit.setCreateTime(LocalDateTime.now());
            audit.setUserFileId(userFileId);
            iFileAuditService.save(audit);
        }
        else if(fileAudit.getAuditStatus().equals(CommonConstant.FILE_AUDIT_ACCESS)){
            //文件审核状态默认为已通过审核  无需修改文件状态

        }else if(fileAudit.getAuditStatus().equals(CommonConstant.FILE_AUDIT_FAIL)){
            //审核失败  - 更新文件审核状态
            final UserFileBo userFileBo = new UserFileBo();
            userFileBo.setUserFileId(userFileId);
            userFileBo.setAuditStatus(CommonConstant.FILE_AUDIT_FAIL);

            List<UserFileBo> userFileList = new ArrayList<>();
            userFileList.add(userFileBo);
            cloudPhotoTransService.updateUserFile(userFileList);
        }
    }
}
