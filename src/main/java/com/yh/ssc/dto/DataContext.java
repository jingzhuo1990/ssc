package com.yh.ssc.dto;

import com.yh.ssc.enmus.HotStateEnums;
import com.yh.ssc.enmus.IndexEnums;
import com.yh.ssc.enmus.P012Enums;
import lombok.Data;

import java.util.List;

/**
 * @program: mediAsst
 * @description:
 * @author: yehang
 * @create: 2024-03-30 19:31
 **/
@Data
public class DataContext {
    
    private List<SscDataDTO> sscDataDTOS;
    
    private List<SinglePercent> singlePercents;
    
    private List<P012> p012s;
    
    @Data
    public static class P012{
        private P012Enums p012Enums;
        
        private Double probability;
    }
    
    
    //概率
    @Data
    public static class SinglePercent {
        
        private IndexEnums indexEnums;
        
        private List<Distributed> distributeds;
        
    }
    
    @Data
    public static class Distributed{
        private Integer num;
        
        private HotStateEnums stateEnums;
        
        private Integer cnt;
    }
    
}
