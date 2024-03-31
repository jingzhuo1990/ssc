package com.yh.ssc.service.orm;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yh.ssc.utils.StreamUtils;
import com.yh.ssc.data.dataobject.SscData;
import com.yh.ssc.dto.SscDataDTO;
import com.yh.ssc.data.query.SscDataQuery;

import java.util.List;
import java.util.stream.Collectors;

public interface SscDataOrmService extends IService<SscData> {
    
    SscData add(SscData detail);
    
    default List<SscDataDTO> listByCondition(SscDataQuery query) {
        QueryWrapper<SscData> queryWrapper = new QueryWrapper<>();
        Long id = query.getId();
        Long cycleId =query.getCycleId();
        
        if (id != null && id >0) {
            queryWrapper.eq("id", id);
        }
        if (cycleId!=null && cycleId>0){
            queryWrapper.eq("cycle_id", cycleId);
        }
        List<SscData> sscData = list(queryWrapper);
        List<SscDataDTO> sscDataDTOS = StreamUtils.ofNullable(sscData).map(x->{
            SscDataDTO sscDataDTO = new SscDataDTO();
            sscDataDTO.setId(x.getId());
            sscDataDTO.setCreateTime(x.getCreateTime());
            sscDataDTO.setCycleId(x.getCycleId());
            sscDataDTO.setCycleValue(x.getCycleValue());
            sscDataDTO.setGameId(x.getGameId());
            sscDataDTO.setResult(x.getResult());
            
            List<String> results = JSONArray.parseArray(x.getResult(),String.class);
            sscDataDTO.setWan(Integer.valueOf(results.get(0)));
            sscDataDTO.setQian(Integer.valueOf(results.get(1)));
            sscDataDTO.setBai(Integer.valueOf(results.get(2)));
            sscDataDTO.setShi(Integer.valueOf(results.get(3)));
            sscDataDTO.setGe(Integer.valueOf(results.get(4)));
            return sscDataDTO;
        }).collect(Collectors.toList());
        return sscDataDTOS;
    }
}
