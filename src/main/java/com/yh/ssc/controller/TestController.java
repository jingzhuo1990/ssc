package com.yh.ssc.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yh.ssc.enmus.FeatureEnums;
import com.yh.ssc.imitate.ImitateService;
import com.yh.ssc.profit.ProfitProperties;
import com.yh.ssc.profit.ProfitService;
import com.yh.ssc.profit.Profit;
import com.yh.ssc.constants.Common;
import com.yh.ssc.dto.DataContext;
import com.yh.ssc.service.DataFactoryService;
import com.yh.ssc.service.result.SscDataResultService;
import com.yh.ssc.strategy.SingleHotStrategyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Resource
    private ProfitService profitService;
    @Resource
    private ProfitProperties profitProperties;
    @Resource
    private SscDataResultService sscDataResultService;
    @Resource
    private ImitateService imitateService;
    
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
        return dataFactoryService.buildDataContext(190,row);
    }
    
    @GetMapping("/profit")
    public List<Profit> getProfit(){
        return profitProperties.getProfits();
    }
    
    @PostMapping("/profit")
    public List<Profit> getProfit(@RequestBody String string){
        List<Profit> profits = JSONArray.parseArray(string, Profit.class);
        profitProperties.setProfits(profits);
        return profitProperties.getProfits();
    }
    
    @GetMapping("/profit/map")
    public Map<Integer, Profit> getProfitMap(){
        return profitService.getProfitsMap();
    }
    
    @GetMapping("interval/{cnt}")
    public String getInterval(@PathVariable Integer cnt){
        try {
            return JSON.toJSONString(sscDataResultService.distribution(Common.TX_QI_QU_FENFEN,cnt,null,null));
        }catch (Exception e){
            log.error("error:{}",e);
        }
        return null;
    }
    
    @GetMapping("imitate/{gameId}/{cnt}/{startId}/{endId}")
    public Map<Integer, Double> imitate(@PathVariable Integer gameId,@PathVariable Integer cnt,@PathVariable Long startId,@PathVariable Long endId) {
        Map<Integer, Double> map = new HashMap<>();
        for (int cntIndex = cnt; cntIndex <= 80; cntIndex++) {
            Double profit = imitateService.imitate(gameId,cntIndex,startId,endId);
            map.put(cntIndex, profit);
        }
        return map;
    }
    
    @GetMapping("baozi/{cnt}/{startId}/{endId}")
    public String imitateBaozi(@PathVariable Integer cnt,@PathVariable Long startId,@PathVariable Long endId) {
        Map<Integer,Map<FeatureEnums,List<Integer>>> baozi = sscDataResultService.baozi(Common.TX_QI_QU_FENFEN,cnt,startId,endId);
        return JSONArray.toJSONString(baozi);
    }
    
    
    @GetMapping("hasbaozi/{cnt}/{startId}/{endId}")
    public String imitateHasBaozi(@PathVariable Integer cnt,@PathVariable Long startId,@PathVariable Long endId) {
        Map<Boolean,List<Integer>> baozi = sscDataResultService.hasBaozi(Common.TX_QI_QU_FENFEN,cnt,startId,endId);
        return JSONArray.toJSONString(baozi);
    }
    
}
