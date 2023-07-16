package com.cloud.photo.trans;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.cloud.photo.trans.mapper"})
@Slf4j
public class TransApplication {
    public static void main(String[] args) {
        SpringApplication.run(TransApplication.class,args);
    }
}
