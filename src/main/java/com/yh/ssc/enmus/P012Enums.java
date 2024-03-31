package com.yh.ssc.enmus;

public enum P012Enums {
    P0(0,"0路"),
    P1(1,"1路"),
    P2(2,"2路"),
    ;
    
    private Integer num;
    
    private String desc;
    
    P012Enums(Integer num, String desc) {
        this.num = num;
        this.desc = desc;
    }
}
