package com.yh.ssc.service.result;

import com.yh.ssc.enmus.IndexEnums;

import java.util.List;
import java.util.Map;

public interface SscDataResultService {
    
    
    List<Integer> query(Long gameId,Long cycleId);
    
    
    Map<String, Map<Integer,List<Integer>>> distribution(Integer gameId,Integer cnt,Long startId,Long endId);
    
    
    void baozi(Integer gameId,Long startId,Long endId);
}
