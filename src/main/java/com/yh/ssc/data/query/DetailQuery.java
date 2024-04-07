package com.yh.ssc.data.query;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-03-31 15:34
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailQuery {
    
    private Long id;
    private Long planId;
    private Long gameId;
    private Integer round;
    private Integer state;
    private Integer amount;
    private BigDecimal profit;
    private Long cycleId;
    private String cycleValue;

}
