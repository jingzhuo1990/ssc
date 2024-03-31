package com.yh.ssc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
@MapperScan(value = {"com.yh.ssc.data"})
public class Main {
    
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}