package com.yh.ssc.enmus;

public enum TypeEnums {
    DAN_WAN("dan","wan"),
    DAN_QIAN("dan","qian"),
    DAN_BAI("dan","bai"),
    DAN_SHI("dan","shi"),
    DAN_GE("dan","ge"),
    ;
    
    
    private String DAN_HAO;
    private String SUB_DAN;
    
    TypeEnums(String DAN_HAO, String SUB_DAN) {
        this.DAN_HAO = DAN_HAO;
        this.SUB_DAN = SUB_DAN;
    }
}
