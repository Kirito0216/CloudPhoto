package com.cloud.photo.image.consumer;


import com.alibaba.fastjson.JSONObject;
import com.cloud.photo.image.service.IFileResizeIconService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ImageConsumer {
    @Autowired
    IFileResizeIconService iFileResizeIconService;

    // 消费监听
    @KafkaListener(topics = {"file_image_topic"})
    public void onMessage1(ConsumerRecord<String, Object> record){
        // 消费的哪个topic、partition的消息,打印出消息内容
        System.out.println("消费："+record.topic()+"-"+record.partition()+"-"+record.value());
        Object value = record.value();
        JSONObject jsonObject = JSONObject.parseObject(value.toString());
        String userFileId = jsonObject.getString("userFileId");
        String storageObjectId = jsonObject.getString("storageObjectId");
        String fileName = jsonObject.getString("fileName");

        iFileResizeIconService.imageThumbnailAndMediaInfo(storageObjectId,fileName);
    }
}
