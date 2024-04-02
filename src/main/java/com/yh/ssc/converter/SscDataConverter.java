package com.yh.ssc.converter;

import com.alibaba.fastjson.JSONArray;
import com.yh.ssc.data.dataobject.SscData;
import com.yh.ssc.dto.SscDataDTO;

import java.util.List;


public class SscDataConverter {
    public static SscDataDTO toDTO(SscData sscData){
        SscDataDTO sscDataDTO = new SscDataDTO();
        sscDataDTO.setId(sscData.getId());
        sscDataDTO.setGameId(sscData.getGameId());
        sscDataDTO.setCreateTime(sscData.getCreateTime());
        sscDataDTO.setCycleId(sscData.getCycleId());
        sscDataDTO.setCycleValue(sscData.getCycleValue());
        
        SscDataDTO.LastData lastData = new SscDataDTO.LastData();
        sscDataDTO.setLastData(lastData);
        
        lastData.setLastCycleValue(sscData.getLastCycleValue());
        lastData.setLastCycleId(sscData.getLastCycleId());
        lastData.setResult(sscData.getResult());
        
        List<String> results = JSONArray.parseArray(sscData.getResult(),String.class);
        lastData.setWan(Integer.valueOf(results.get(0)));
        lastData.setQian(Integer.valueOf(results.get(1)));
        lastData.setBai(Integer.valueOf(results.get(2)));
        lastData.setShi(Integer.valueOf(results.get(3)));
        lastData.setGe(Integer.valueOf(results.get(4)));
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
