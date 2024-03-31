package com.yh.ssc.enmus;

public enum IndexEnums {
    WAN(0,"万位"),
    QIAN(1,"千位"),
    BAI(2,"百位"),
    SHI(3,"十位"),
    GE(4,"个位");
    
    private Integer index;
    private String desc;
    
    IndexEnums(Integer index, String desc) {
        this.index = index;
        this.desc = desc;
    }
    
    public Integer getIndex() {
        return index;
    }
    
    public String getDesc() {
        return desc;
    }
}
