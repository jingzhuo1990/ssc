package com.yh.ssc.service;

import com.alibaba.fastjson.JSONObject;
import com.yh.ssc.conf.GameProperteries;
import com.yh.ssc.converter.SscDataConverter;
import com.yh.ssc.dto.QueryData;
import com.yh.ssc.data.dataobject.SscData;
import com.yh.ssc.dto.SscDataDTO;
import com.yh.ssc.data.query.SscDataQuery;
import com.yh.ssc.service.orm.SscDataOrmService;
import com.yh.ssc.utils.StreamUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @program: mediAsst
 * @description:
 * @author: yehang
 * @create: 2024-03-29 22:04
 **/
@Component
@Slf4j
public class SaveDataService implements Runnable{
    
    @Resource
    private SscService sscRealService;
    @Resource
    private SscDataOrmService sscDataOrmService;
    @Resource
    private GameProperteries gameProperteries;
    
    @Override
    public void run() {
        StreamUtils.ofNullable(gameProperteries.getGames()).forEach(game->{
            addPlatformData(game.getGameId(),game.getRow());
        });
    }
    
    private void addPlatformData(Integer gameId,Integer rowCnt){
        try {
            //这里只需要查询30个
            QueryData queryData = sscRealService.query(gameId,30);
            SscData nowCycle = new SscData();
            if (Objects.isNull(queryData.getLottery_cycle_now().getLast_cycle_game_result())){
                return;
            }
            nowCycle.setGameId(queryData.getGame_id());
            nowCycle.setRow(rowCnt);
            nowCycle.setCycleId(queryData.getLottery_cycle_now().getNow_cycle_id());
            nowCycle.setCycleValue(queryData.getLottery_cycle_now().getNow_cycle_value());
            nowCycle.setLastCycleValue(queryData.getLottery_cycle_now().getLast_cycle_value());
            nowCycle.setLastResult(queryData.getLottery_cycle_now().getLast_cycle_game_result());
            
            List<SscDataDTO> sscDatas = sscDataOrmService.listByCondition(SscDataQuery.builder().gameId(nowCycle.getGameId()).cycleId(nowCycle.getCycleId()).build());
            if (CollectionUtils.isNotEmpty(sscDatas)){
//                log.warn("already exist");
            }else {
                String lastRst = JSONObject.toJSONString(queryData.getLottery_cycle_now().getLast_cycle_game_result());
                SscData lastSscData = updateLastCycle(
                        nowCycle.getGameId(),queryData.getLottery_cycle_now().getLast_cycle_value(),lastRst);
                if (lastSscData!=null){
                    nowCycle.setLastCycleId(lastSscData.getCycleId());
                }
                sscDataOrmService.add(nowCycle);
            }
        }catch (Throwable e) {
            log.error("error run save data,{}",e);
        }
    }
    
    private SscData updateLastCycle(Long gameId,String lastCycleValue,String result){
        List<SscDataDTO> sscDataDTOS = sscDataOrmService.listByCondition(SscDataQuery.builder().gameId(gameId).cycleValue(lastCycleValue).build());
        if (CollectionUtils.isEmpty(sscDataDTOS)){
            return null;
        }
        SscDataDTO sscDataDTO = sscDataDTOS.get(0);
        sscDataDTO.getLastData().setResult(result);
        SscData sscData = SscDataConverter.toDO(sscDataDTO);
        sscDataOrmService.updateById(sscData);
        return sscData;
    }
}
