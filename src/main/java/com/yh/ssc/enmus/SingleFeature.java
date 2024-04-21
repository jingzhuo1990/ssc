package com.yh.ssc.enmus;

public enum SingleFeature {
    DA("大"),
    XIAO("小"),
    DAN("单"),
    SHUANG("双"),
    ;
    
    private String desc;
    
    
    SingleFeature(String desc) {
        this.desc = desc;
    }
    
    public static SingleFeature daxiao(Integer num){
        if (num>=5){
            return DA;
        }
        return XIAO;
    }
    
    public static SingleFeature danshuang(Integer num){
        if (num % 2 ==0){
            return SHUANG;
        }
        return DAN;
    }
}
