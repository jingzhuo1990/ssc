package com.yh.ssc.dto;

import com.baomidou.mybatisplus.annotation.TableName;
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
public class SscDataDTO {
    
    private Long id;
    
    private Long gameId;
    
    private Long cycleId;
    
    private String cycleValue;
    
    private String result;
    
    private Integer wan;
    
    private Integer qian;
    
    private Integer bai;
    
    private Integer shi;
    
    private Integer ge;
    
    private Date createTime;
}
