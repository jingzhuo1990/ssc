package com.yh.ssc.imitate;

import com.google.common.util.concurrent.AtomicDouble;
import com.yh.ssc.constants.Common;
import com.yh.ssc.profit.Profit;
import com.yh.ssc.profit.ProfitService;
import com.yh.ssc.service.result.SscDataResultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-04-03 14:27
 **/
@Component
@Slf4j
public class ImitateService {
    
    @Resource
    private ProfitService profitService;
    @Resource
    private SscDataResultService sscDataResultService;
    
    private static AtomicDouble mostCost = new AtomicDouble(0);
    
    private static AtomicInteger totalPlan = new AtomicInteger(0);
    
    public double imitate(Integer gameId,Integer cnt,Long startId,Long endId){
        mostCost.set(0);
        totalPlan.set(0);
        Map<String, Map<Integer, List<Integer>>> distribute = sscDataResultService.distribution(gameId,cnt,startId,endId);
        
        double allProfit = 0;
        
        // 开始遍历
        for (Map.Entry<String, Map<Integer, List<Integer>>> outerEntry : distribute.entrySet()) {
            String outerKey = outerEntry.getKey(); // 外层Map的Key
            Map<Integer, List<Integer>> innerMap = outerEntry.getValue(); // 内层Map
            
            for (Map.Entry<Integer, List<Integer>> innerEntry : innerMap.entrySet()) {
                Integer innerKey = innerEntry.getKey(); // 内层Map的Key
                List<Integer> integerList = innerEntry.getValue(); // List<Integer>
                
                // 遍历List<Integer>
                for (Integer number : integerList) {
                    totalPlan.addAndGet(1);
                    // 这里是对List<Integer>中每个Integer的操作
                    allProfit += calcuSingle(number-cnt+1);
                }
            }
        }
        log.info("startId:{},endId:{},gapRound:{},planCnt:{},round:{},mostCost:{}",startId,endId,cnt,totalPlan,profitService.getProfitsMap().size(),mostCost);
        return allProfit;
        
    }
    
    private double calcuSingle(Integer number){
        Map<Integer, Profit> profitMap = profitService.getProfitsMap();
        double allCost = profitMap.values().stream().map(Profit::getCost).mapToDouble(Double::doubleValue).sum();
        if (profitMap.containsKey(number)){
            return profitMap.get(number).getProfit();
        }
        mostCost.addAndGet(allCost);
        return -allCost;
    }
    
}
