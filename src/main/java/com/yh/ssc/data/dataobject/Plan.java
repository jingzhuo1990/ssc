package com.yh.ssc.data.dataobject;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-03-31 15:30
 **/
@Data
@TableName("plan")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Plan {

    private Long id;
    private Integer round;
    private Integer current;
    private Long startCycleId;
    private String StartCycleValue;
    private String policy;
    private Integer state;
    private Date createTime;
    private String candidate;
    private String type;
    private String subType;
    private Integer singleCan;
    
    @TableField(exist = false)
    private List<List<Integer>> candidateInner;
}
