package com.yh.ssc.data.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-03-31 15:34
 **/
@Data
@TableName("detail")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Detail {
    
    private Long id;
    private Long gameId;
    private Long planId;
    private Integer round;
    private Integer multify;
    private Integer state;
    private Integer amount;
    private BigDecimal profit;
    private Date createTime;
    private Long cycleId;
    private String cycleValue;
}
