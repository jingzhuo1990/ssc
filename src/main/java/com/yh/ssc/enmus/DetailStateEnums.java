package com.yh.ssc.enmus;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-03-31 15:32
 **/
public enum DetailStateEnums {
    
    RUNNING(1,"进行中"),
    
    SEND_OK(2,"下成"),
    FAILED(9,"失败"),
    SUCCESS(100,"成功")
    
    ;
    
    
    private Integer state;
    private String desc;
    
    DetailStateEnums(Integer state, String desc) {
        this.state = state;
        this.desc = desc;
    }
    
    public Integer getState() {
        return state;
    }
    
    public String getDesc() {
        return desc;
    }
}
