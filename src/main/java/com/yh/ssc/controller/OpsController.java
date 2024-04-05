package com.yh.ssc.controller;

import com.alibaba.fastjson.JSONArray;
import com.yh.ssc.conf.PlatformConfig;
import com.yh.ssc.profit.Profit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-04-05 13:49
 **/
@RestController
@RequestMapping("/ops")
@Slf4j
public class OpsController {
    
    @Resource
    private PlatformConfig platformConfig;
    
    @PostMapping("/auth")
    public String setAuth(@RequestBody String string){
        log.info("auth ops:{}",string);
        platformConfig.setAuth(string);
        return platformConfig.getAuth();
    }
    
    @GetMapping("/auth")
    public String getAuth(){
        return platformConfig.getAuth();
    }
}
