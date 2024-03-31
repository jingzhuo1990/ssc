package com.yh.ssc.enmus;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-03-31 15:32
 **/
public enum PlanStateEnums {
    
    RUNNING(1,"进行中"),
    FAILED(9,"失败"),
    SUCCESS(100,"成功")
    
    ;
    
    
    private Integer state;
    private String desc;
    
    PlanStateEnums(Integer state, String desc) {
        this.state = state;
        this.desc = desc;
    }
}
