package com.yh.ssc.data.dataobject;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("ssc_data")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SscData {
    
    private Long id;
    
    private Long gameId;
    
    private Long cycleId;
    
    private String cycleValue;
    
    private Long lastCycleId;
    
    private String lastCycleValue;
    
    private String result;
    
    private Date createTime;
    
    @TableField(exist = false)
    private List<String> lastResult;
    
    @TableField(exist = false)
    private Integer row;
}
