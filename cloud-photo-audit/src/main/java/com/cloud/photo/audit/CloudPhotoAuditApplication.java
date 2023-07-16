package com.cloud.photo.audit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author 11446
 */
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan(basePackages = {"com.cloud.photo.audit.mapper"})
@ComponentScan({"com.cloud.photo"})
@EnableFeignClients(basePackages = {"com.cloud.photo"})
public class CloudPhotoAuditApplication {
    public static void main(String[] args) {

        SpringApplication.run(CloudPhotoAuditApplication.class,args);
    }
}