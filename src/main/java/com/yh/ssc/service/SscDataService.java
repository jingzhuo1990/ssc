package com.yh.ssc.service;

import com.yh.ssc.dto.QueryData;

import java.util.List;

public interface SscDataService {
    
    QueryData query(Integer gameId,Integer rowCnt);
    
    void send(Long cycleId,Integer multiple,List<List<Integer>> data);
}
