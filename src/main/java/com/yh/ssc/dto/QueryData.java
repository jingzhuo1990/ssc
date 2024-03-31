package com.yh.ssc.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @program: mediAsst
 * @description:
 * @author: yehang
 * @create: 2024-03-29 21:50
 **/
@Data
public class QueryData {
    private Long game_id;
    private String game_value;
    private CycleNow lottery_cycle_now;
    
    private List<ResultHis> lottery_result_history;
    
    @Data
    public static class CycleNow{
        private Long now_cycle_id;
        private String now_cycle_value;
        private String last_cycle_value;
        private List<String> last_cycle_game_result;
    }
    
    @Data
    public static class ResultHis{
        private String cycle_value;
        private List<String> game_result;
        private Date open_time;
        
        
    }
}
