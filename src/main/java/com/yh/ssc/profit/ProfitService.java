package com.yh.ssc.profit;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-04-02 19:30
 **/
@Service
public class ProfitService {
    
    @Resource
    private ProfitProperties profitProperties;
    
    private Map<Integer, Profit> profitsMap;
    
    
    public Map<Integer, Profit> getProfitsMap() {
        profitsMap = profitProperties.getProfits().stream()
                .collect(Collectors.toMap(Profit::getRound, profit -> profit));
        return profitsMap;
    }
    
    public void setProfitsMap(Map<Integer, Profit> profitsMap){
        this.profitsMap = profitsMap;
    }
}
