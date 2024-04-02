package com.yh.ssc.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.yh.ssc.converter.SscDataConverter;
import com.yh.ssc.dto.QueryData;
import com.yh.ssc.data.dataobject.SscData;
import com.yh.ssc.dto.SscDataDTO;
import com.yh.ssc.data.query.SscDataQuery;
import com.yh.ssc.service.orm.SscDataOrmService;
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
    private SscDataService sscDataRealService;
    @Resource
    private SscDataOrmService sscDataOrmService;
    
    
    @Override
    public void run() {
        try {
            QueryData queryData = sscDataRealService.query(190,30);
            SscData nowCycle = new SscData();
            if (Objects.isNull(queryData.getLottery_cycle_now().getLast_cycle_game_result())){
                return;
            }
            nowCycle.setGameId(queryData.getGame_id());
            nowCycle.setCycleId(queryData.getLottery_cycle_now().getNow_cycle_id());
            nowCycle.setCycleValue(queryData.getLottery_cycle_now().getLast_cycle_value());
            nowCycle.setLastCycleValue(queryData.getLottery_cycle_now().getLast_cycle_value());
            
            List<SscDataDTO> sscDatas = sscDataOrmService.listByCondition(SscDataQuery.builder().cycleId(nowCycle.getCycleId()).build());
            if (CollectionUtils.isNotEmpty(sscDatas)){
                log.warn("already exist");
            }else {
                String lastRst = JSONObject.toJSONString(queryData.getLottery_cycle_now().getLast_cycle_game_result());
                SscData lastSscData = updateLastCycle(queryData.getLottery_cycle_now().getLast_cycle_value(),lastRst);
                nowCycle.setLastCycleId(lastSscData.getCycleId());
                sscDataOrmService.add(nowCycle);
            }
        }catch (Throwable e) {
            log.error("error run save data,{}",e);
        }
    }
    
    private SscData updateLastCycle(String lastCycleValue,String result){
        List<SscDataDTO> sscDataDTOS = sscDataOrmService.listByCondition(SscDataQuery.builder().cycleValue(lastCycleValue).build());
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(sscDataDTOS),"查找不到上一轮数据 "+lastCycleValue);
        SscDataDTO sscDataDTO = sscDataDTOS.get(0);
        sscDataDTO.getLastData().setResult(result);
        SscData sscData = SscDataConverter.toDO(sscDataDTO);
        sscDataOrmService.updateById(sscData);
        return sscData;
    }
}
