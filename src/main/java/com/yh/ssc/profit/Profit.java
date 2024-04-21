package com.yh.ssc.profit;

import lombok.Data;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-04-02 19:10
 **/
@Data
public class Profit {
    
    private Integer round;
    
    private Integer multify;
    
    private Double cost;
    
    private Double profit;
    
    private Double totalCost;
}
