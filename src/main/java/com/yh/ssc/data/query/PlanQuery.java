package com.yh.ssc.data.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class PlanQuery {

    private Long id;
    private Integer round;
    private Integer current;
    private Long startCycleId;
    private String startCycleValue;
    private List<String> cycleValues;
    private String policy;
    private Integer state;
    private String type;
    private String subType;
    private Integer singleCan;
}
