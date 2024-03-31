package com.yh.ssc.service;

import com.yh.ssc.dto.QueryData;
import com.yh.ssc.service.orm.SscDataOrmService;
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
public class SscDataSimulateService implements SscDataService{
    
    @Resource
    private SscDataOrmService sscDataOrmService;
    
    @Override
    public QueryData query(Integer gameId, Integer rowCnt) {
        return null;
    }
    
    @Override
    public void send(Long cycleId, Integer multiple, List<List<String>> data) {
    
    }
}
