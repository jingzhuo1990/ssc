package com.yh.ssc.service.orm;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yh.ssc.converter.SscDataConverter;
import com.yh.ssc.utils.StreamUtils;
import com.yh.ssc.data.dataobject.SscData;
import com.yh.ssc.dto.SscDataDTO;
import com.yh.ssc.data.query.SscDataQuery;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public interface SscDataOrmService extends IService<SscData> {
    
    SscData add(SscData detail);
    
    default List<SscDataDTO> listByCondition(SscDataQuery query) {
        QueryWrapper<SscData> queryWrapper = new QueryWrapper<>();
        Long id = query.getId();
        Long cycleId =query.getCycleId();
        String cycleValue = query.getCycleValue();
        Long gameId = query.getGameId();
        Date startTime = query.getStartTime();
        Date endTime = query.getEndTime();
        Long startId = query.getStartId();
        Long endId = query.getEndId();
        
        if (id != null && id >0) {
            queryWrapper.eq("id", id);
        }
        if (startId!=null && endId!=null){
            queryWrapper.between("id",startId,endId);
        }
        if (cycleId!=null && cycleId>0){
            queryWrapper.eq("cycle_id", cycleId);
        }
        if (StringUtils.isNotEmpty(cycleValue)){
            queryWrapper.eq("cycle_value", cycleValue);
        }
        if (gameId!=null && gameId>0){
            queryWrapper.eq("game_id", gameId);
        }
        if (startTime!=null && endTime!=null){
            queryWrapper.between("create_time", startTime, endTime);
        }
        
        List<SscData> sscData = list(queryWrapper);
        List<SscDataDTO> sscDataDTOS = StreamUtils.ofNullable(sscData)
                .map(x-> SscDataConverter.toDTO(x))
                .collect(Collectors.toList());
        return sscDataDTOS;
    }
    
}
