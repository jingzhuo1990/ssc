package com.yh.ssc.service;

import com.alibaba.fastjson.JSONObject;
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
            SscData last = new SscData();
            if (Objects.isNull(queryData.getLottery_cycle_now().getLast_cycle_game_result())){
                return;
            }
            last.setGameId(queryData.getGame_id());
            last.setCycleId(queryData.getLottery_cycle_now().getNow_cycle_id()-1);
            last.setCycleValue(queryData.getLottery_cycle_now().getLast_cycle_value());
            last.setResult(JSONObject.toJSONString(queryData.getLottery_cycle_now().getLast_cycle_game_result()));
            
            List<SscDataDTO> sscDatas = sscDataOrmService.listByCondition(SscDataQuery.builder().cycleId(last.getCycleId()).build());
            if (CollectionUtils.isNotEmpty(sscDatas)){
                log.warn("already exist");
            }else {
                sscDataOrmService.add(last);
            }
        }catch (Throwable e) {
            log.error("error run save data,{}",e);
        }
        
    }
}
