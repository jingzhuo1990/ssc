package com.yh.ssc.service;

import com.alibaba.fastjson.JSONArray;
import com.yh.ssc.dto.QueryData;
import com.yh.ssc.service.orm.SscDataOrmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: ssc
 * @description: 模拟
 * @author: yehang
 * @create: 2024-03-31 13:32
 **/
@Component
@Slf4j
public class SscDataSimulateService implements SscDataService{
    
    @Resource
    private SscDataOrmService sscDataOrmService;
    
    @Override
    public QueryData query(Integer gameId, Integer rowCnt) {
        return null;
    }
    
    @Override
    public void send(Long cycleId, Integer multiple, List<List<Integer>> data) {
        log.info("mock send,cycleId:{},multiple:{},data:{}",cycleId,multiple, JSONArray.toJSONString(data));
    }
}
