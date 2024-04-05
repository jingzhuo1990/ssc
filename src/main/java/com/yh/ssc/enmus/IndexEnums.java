package com.yh.ssc.enmus;

import org.apache.commons.lang3.StringUtils;

public enum IndexEnums {
    WAN(0,"万位","SINGLE"),
    QIAN(1,"千位","SINGLE"),
    BAI(2,"百位","SINGLE"),
    SHI(3,"十位","SINGLE"),
    GE(4,"个位","SINGLE");
    
    private Integer index;
    private String subType;
    private String type;
    
    
    IndexEnums(Integer index, String subType) {
        this.index = index;
        this.subType = subType;
    }
    
    IndexEnums(Integer index, String subType, String type) {
        this.index = index;
        this.subType = subType;
        this.type = type;
    }
    
    public Integer getIndex() {
        return index;
    }
    
    public String getSubType() {
        return subType;
    }
    
    public String getType() {
        return type;
    }
    
    public static IndexEnums getBySubType(String subType){
        for (IndexEnums index:IndexEnums.values()) {
            if (StringUtils.equals(index.getSubType(),subType)){
                return index;
            }
        }
        throw new RuntimeException("cannot find index enums");
    }
}
