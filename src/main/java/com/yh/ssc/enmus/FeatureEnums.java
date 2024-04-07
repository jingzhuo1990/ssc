package com.yh.ssc.enmus;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-04-05 15:56
 **/
public enum FeatureEnums {
    BAOZI(1,"baozi"),
    ZU_SAN(2,"zu_3"),
    ZU_LIU(3,"zu_6"),
    ;
    
    
    private Integer feature;
    
    private String desc;
    
    public Integer getFeature() {
        return feature;
    }
    
    public String getDesc() {
        return desc;
    }
    
    FeatureEnums(Integer feature, String desc) {
        this.feature = feature;
        this.desc = desc;
    }
    
    public static FeatureEnums build(Integer num1,Integer num2,Integer num3){
        if(num1.equals(num2)  && num2.equals(num3) ){
            return BAOZI;
        }else if(num1.equals(num2) || num2.equals(num3) || num1.equals(num3)){
            return ZU_SAN;
        }else{
            return ZU_LIU;
        }
    }
    
    public static boolean isBaozi(FeatureEnums featureEnum){
        if (BAOZI.getFeature().intValue() == featureEnum.getFeature().intValue()){
            return true;
        }
        return false;
    }
    
    public static boolean isZu3(FeatureEnums featureEnum){
        if (ZU_SAN.getFeature().intValue() == featureEnum.getFeature().intValue()){
            return true;
        }
        return false;
    }
}
