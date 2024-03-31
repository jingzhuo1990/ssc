package com.yh.ssc.scheduler;

import com.yh.ssc.service.SaveDataService;
import jdk.internal.util.EnvUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @program: mediAsst
 * @description:
 * @author: yehang
 * @create: 2024-03-29 11:57
 **/
@Component
public class SchedulerService{
    
    @Resource
    private SaveDataService saveDataService;
    
    @Resource
    private Environment environment;
    
    @EventListener(ApplicationReadyEvent.class)
    public void execute(){
        // 获取当前激活的 profiles
        String[] activeProfiles = environment.getActiveProfiles();
        
        // 检查是否 "local" profile 激活
        boolean isLocalActive = Arrays.asList(activeProfiles).contains("local");
        
        if (isLocalActive){
            System.out.println("is local");
            return;
        }
        System.out.println("not local");
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        
        // 指定任务在1秒后开始执行，然后每2秒执行一次
        executor.scheduleAtFixedRate(saveDataService, 1, 2, TimeUnit.SECONDS);
    }
    
    
    
}
