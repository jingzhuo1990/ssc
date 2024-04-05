package com.yh.ssc.converter;

import com.alibaba.fastjson.JSONArray;
import com.yh.ssc.data.dataobject.SscData;
import com.yh.ssc.dto.SscDataDTO;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;


public class SscDataConverter {
    public static SscDataDTO toDTO(SscData sscData){
        SscDataDTO sscDataDTO = new SscDataDTO();
        sscDataDTO.setId(sscData.getId());
        sscDataDTO.setGameId(sscData.getGameId());
        sscDataDTO.setCreateTime(sscData.getCreateTime());
        sscDataDTO.setCycleId(sscData.getCycleId());
        sscDataDTO.setCycleValue(sscData.getCycleValue());
        
        SscDataDTO.LastData nowData = new SscDataDTO.LastData();
        sscDataDTO.setNowData(nowData);
        nowData.setResult(JSONArray.toJSONString(sscData.getResult()));
        List<String> nowResults = JSONArray.parseArray(sscData.getResult(),String.class);
        if (CollectionUtils.isNotEmpty(nowResults)){
            nowData.setWan(Integer.valueOf(nowResults.get(0)));
            nowData.setQian(Integer.valueOf(nowResults.get(1)));
            nowData.setBai(Integer.valueOf(nowResults.get(2)));
            nowData.setShi(Integer.valueOf(nowResults.get(3)));
            nowData.setGe(Integer.valueOf(nowResults.get(4)));
        }
        
        SscDataDTO.LastData lastData = new SscDataDTO.LastData();
        sscDataDTO.setLastData(lastData);
        
        lastData.setLastCycleValue(sscData.getLastCycleValue());
        lastData.setLastCycleId(sscData.getLastCycleId());
        lastData.setResult(JSONArray.toJSONString(sscData.getLastResult()));
        
        List<String> results = sscData.getLastResult();
        if (CollectionUtils.isNotEmpty(results)){
            lastData.setWan(Integer.valueOf(results.get(0)));
            lastData.setQian(Integer.valueOf(results.get(1)));
            lastData.setBai(Integer.valueOf(results.get(2)));
            lastData.setShi(Integer.valueOf(results.get(3)));
            lastData.setGe(Integer.valueOf(results.get(4)));
        }
        
        return sscDataDTO;
    }
    
    public static SscData toDO(SscDataDTO sscDataDTO){
        SscData sscData = new SscData();
        sscData.setId(sscDataDTO.getId());
        sscData.setCreateTime(sscDataDTO.getCreateTime());
        sscData.setCycleId(sscDataDTO.getCycleId());
        sscData.setCycleValue(sscDataDTO.getCycleValue());
        sscData.setLastCycleValue(sscDataDTO.getLastData().getLastCycleValue());
        
        sscData.setGameId(sscDataDTO.getGameId());
        sscData.setResult(sscDataDTO.getLastData().getResult());
        return sscData;
    }
}
