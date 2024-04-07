package com.yh.ssc.service.result;

import com.yh.ssc.enmus.FeatureEnums;
import com.yh.ssc.enmus.IndexEnums;

import java.util.List;
import java.util.Map;

public interface SscDataResultService {
    
    
    List<Integer> query(Long gameId,Long cycleId);
    
    
    Map<String, Map<Integer,List<Integer>>> distribution(Integer gameId,Integer cnt,Long startId,Long endId);
    
    
    Map<Integer,Map<FeatureEnums,List<Integer>>> baozi(Integer gameId,Integer cnt,Long startId,Long endId);
    
    Map<Boolean,List<Integer>> hasBaozi(Integer gameId,Integer cnt,Long startId,Long endId);
}
