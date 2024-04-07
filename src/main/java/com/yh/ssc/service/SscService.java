package com.yh.ssc.service;

import com.yh.ssc.dto.QueryData;

import java.util.List;

public interface SscService {
    
    QueryData query(Integer gameId,Integer rowCnt);
    
    String send(Long gameId,Long cycleId,Integer multiple,List<List<Integer>> data);
}
