package com.yh.ssc.enmus;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-03-31 10:15
 **/
public enum HotStateEnums {
    
    HOT("hot","热"),
    MID("mid","温"),
    COLD("cold","冷"),
    ;
    
    private String name;
    private String desc;
    
    HotStateEnums(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }
}
