package com.yh.ssc.data.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

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
    
    private Long startId;
    
    private Long endId;
    
    private Long gameId;
    
    private Long cycleId;
    
    private String cycleValue;
    
    private List<String> cycleValues;
    
    private String result;
    
    private Date startTime;
    
    private Date endTime;
    
    private Integer limit;
}
