package com.yh.ssc.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.yh.ssc.conf.GameProperteries;
import com.yh.ssc.conf.PlatformConfig;
import com.yh.ssc.dto.QueryData;
import com.yh.ssc.service.SscService;
import com.yh.ssc.utils.StreamUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
    
    @GetMapping("/testSend")
    public List<SendClass> testSend(){
        return StreamUtils.ofNullable(gameProperteries.getGames()).filter(x->x.getPlan()).map(game->{
            Integer gameId = game.getGameId();
            QueryData queryData = sscRealService.query(gameId,20);
            Long cycleId = queryData.getLottery_cycle_now().getNow_cycle_id();
            String candidate = "[[0],[],[],[],[]]";
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
