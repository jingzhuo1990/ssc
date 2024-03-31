package com.yh.ssc.data.query;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String StartCycleValue;
    private String policy;
    private Integer state;
}
