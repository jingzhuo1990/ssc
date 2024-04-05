package com.yh.ssc.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-03-31 15:30
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlanDTO {

    private Long id;
    private Integer round;
    private Integer current;
    private Long startCycleId;
    private String startCycleValue;
    private String policy;
    private Integer state;
    private Date createTime;
    private String candidate;
    private String type;
    private String subType;
    private Integer singleCan;
    
    private JSONObject policyJSON;
    
    private List<List<Integer>> candidateInner;
}
