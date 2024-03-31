package com.yh.ssc.data.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @program: mediAsst
 * @description:
 * @author: yehang
 * @create: 2024-03-29 10:57
 **/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SscDataQuery {
    
    private Long id;
    
    private Long gameId;
    
    private Long cycleId;
    
    private Long cycleValue;
    
    private String result;
    
    private Date createTime;
    
    private Integer limit;
}
