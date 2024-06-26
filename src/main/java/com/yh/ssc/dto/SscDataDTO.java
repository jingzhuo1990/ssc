package com.yh.ssc.dto;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yh.ssc.enmus.FeatureEnums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @program: mediAsst
 * @description:
 * @author: yehang
 * @create: 2024-03-29 10:57
 **/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SscDataDTO {
    
    private Long id;
    
    private Long gameId;
    
    private Integer row;
    
    private Long cycleId;
    
    private String cycleValue;
    
    private LastData lastData;
    
    private LastData nowData;
    
    private Date createTime;
    
    @Data
    public static class LastData{
        
        private Long lastCycleId;
        
        private String lastCycleValue;
        
        private String result;
        
        private Integer wan;
        
        private Integer qian;
        
        private Integer bai;
        
        private Integer shi;
        
        private Integer ge;
        
        private FeatureEnums front3;
        private FeatureEnums mid3;
        private FeatureEnums last3;
        
    }
    
    public Integer getLastSum(){
        List<Integer> resultInt = JSONArray.parseArray(getLastData().getResult(),Integer.class);
        return resultInt.get(3)+resultInt.get(4);
    }
    
    public Integer getByIndex(int index) {
        List<Integer> resultInt = JSONArray.parseArray(getLastData().getResult(),Integer.class);
        return resultInt.get(index);
    }
}
