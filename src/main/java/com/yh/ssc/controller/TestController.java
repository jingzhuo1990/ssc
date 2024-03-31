package com.yh.ssc.controller;

import com.alibaba.fastjson.JSONArray;
import com.yh.ssc.dto.DataContext;
import com.yh.ssc.service.DataFactoryService;
import com.yh.ssc.strategy.SingleHotStrategyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: mediAsst
 * @description: tes
 * @author: yehang
 * @create: 2024-03-30 09:53
 **/
@RestController
@RequestMapping("/ssc")
@Slf4j
public class TestController {
    
    @Resource
    private SingleHotStrategyService singleHotStrategyService;
    
    @Resource
    private DataFactoryService dataFactoryService;
    
    @GetMapping("/test")
    public String testRecommend(){
        try {
            List<List<String>> recomm = singleHotStrategyService.recommend(null);
            return JSONArray.toJSONString(recomm);
        }catch (Throwable e){
            log.error("test error:{}",e);
        }
        return "";
    }
    
    
    @GetMapping("single/{row}")
    public DataContext single(@PathVariable Integer row){
        return dataFactoryService.buildDataContext(row);
    }
}
