package com.yh.ssc.dto;

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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailDTO {
    
    private Long id;
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
