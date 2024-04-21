package com.yh.ssc.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.yh.ssc.conf.GameProperteries;
import com.yh.ssc.conf.PlatformConfig;
import com.yh.ssc.data.dataobject.SscData;
import com.yh.ssc.data.query.SscDataQuery;
import com.yh.ssc.dto.QueryData;
import com.yh.ssc.dto.SscDataDTO;
import com.yh.ssc.service.SscService;
import com.yh.ssc.service.orm.SscDataOrmService;
import com.yh.ssc.utils.StreamUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    @Resource
    private GameProperteries gameProperteries;
    @Resource
    private SscService sscRealService;
    @Resource
    private SscDataOrmService sscDataOrmService;
    
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
    
    @GetMapping("/plan")
    public String getPlan(){
        List<GameProperteries.Game> games = gameProperteries.getGames();
        return JSONObject.toJSONString(games);
    }
    
    @PostMapping("/plan")
    public String setPlan(@RequestBody String gamesStr){
        List<GameProperteries.Game> games = JSONArray.parseArray(gamesStr, GameProperteries.Game.class);
        gameProperteries.setGames(games);
        return JSONObject.toJSONString(gameProperteries.getGames());
    }
    
    @GetMapping("/repair/{gameId}/{row}")
    public String repair(@PathVariable Integer gameId,@PathVariable Integer row){
        QueryData queryData = sscRealService.query(gameId,row);
        List<QueryData.ResultHis> resultHis = queryData.getLottery_result_history();
        Long nowCycleId = queryData.getLottery_cycle_now().getNow_cycle_id();
        for (QueryData.ResultHis his : resultHis){
            List<SscDataDTO> sscDataDTOS = sscDataOrmService.listByCondition(SscDataQuery.builder().gameId(
                    Long.valueOf(gameId)).cycleValue(his.getCycle_value()).build());
            if (CollectionUtils.isEmpty(sscDataDTOS)){
                SscData sscData = new SscData();
                sscData.setGameId(Long.valueOf(gameId));
                sscData.setCreateTime(new Date());
                nowCycleId = nowCycleId-1;
                sscData.setCycleId(nowCycleId);
                sscData.setLastCycleId(nowCycleId-1);
                
                String cycleValue = his.getCycle_value();
                sscData.setCycleValue(cycleValue);
                sscData.setLastCycleValue(String.valueOf(Long.valueOf(cycleValue)-1));
                String rst = JSONObject.toJSONString(his.getGame_result());
                sscData.setResult(rst);
                sscDataOrmService.add(sscData);
                log.info("repair sscData successfully:{}",sscData.getCycleValue());
            }else {
                log.warn("{} existed",his.getCycle_value());
                nowCycleId = sscDataDTOS.get(0).getCycleId();
            }
        }
        return "success";
    }
    
    @PostMapping("/mock/data")
    public String addData(@RequestBody String data){
        SscData sscData = JSON.parseObject(data,SscData.class);
        sscData.setCycleValue(String.valueOf(sscData.getCycleId()));
        sscData.setLastCycleId(sscData.getCycleId()-1);
        
        sscData.setLastCycleValue(String.valueOf(Long.valueOf(sscData.getCycleValue())-1));
        String lastCycleValue = sscData.getLastCycleValue();
        List<SscDataDTO> sscDataDTOS = sscDataOrmService.listByCondition(SscDataQuery.builder().cycleValue(lastCycleValue).build());
        
        String rst = JSONObject.parseObject(sscDataDTOS.get(0).getNowData().getResult(),String.class);
        
        sscData.setLastResult(JSON.parseArray(rst,String.class));
        sscData = sscDataOrmService.add(sscData);
        return JSON.toJSONString(sscData);
    }
    
    
    @GetMapping("/testSend")
    public List<SendClass> testSend(){
        return StreamUtils.ofNullable(gameProperteries.getGames()).filter(x->x.getPlan()).map(game->{
            Integer gameId = game.getGameId();
            QueryData queryData = sscRealService.query(gameId,20);
            Long cycleId = queryData.getLottery_cycle_now().getNow_cycle_id();
            String candidate = "[[6,7,8,9,10]]";
            List<List<Integer>> candidateInner = JSON.parseObject(candidate, new TypeReference<List<List<Integer>>>() {});
            String sendRst = sscRealService.send(Long.valueOf(gameId),cycleId,1,candidateInner);
            SendClass sendClass = new SendClass();
            sendClass.setGameId(gameId);
            sendClass.setTestResult(sendRst);
            return sendClass;
        }).collect(Collectors.toList());
    }
    
    @Data
    public static class SendClass{
        private Integer gameId;
        private String testResult;
    }
}
