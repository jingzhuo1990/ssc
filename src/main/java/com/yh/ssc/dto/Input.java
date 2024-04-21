package com.yh.ssc.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class Input {

    private Integer game_id = 190;
//    private Integer game_type_id=65;
    private Integer game_type_id=51;

    private Long game_cycle_id;

    //[[\"0\",\"1\",\"3\",\"4\",\"7\"],[],[],[],[]]
    private String bet_info;

    private String bet_mode = "TwoJiao";

    private Integer bet_multiple;

    private String bet_percent_type = "AdjustPercentType";

    private Integer bet_percent = 0;
    @JSONField(name = "is_follow")
    private boolean follow;
    private Integer follow_commission_percent = null;

}
