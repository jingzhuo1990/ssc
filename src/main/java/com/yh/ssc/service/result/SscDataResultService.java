package com.yh.ssc.service.result;

import java.util.List;

public interface SscDataResultService {
    
    
    List<Integer> query(Long gameId,Long cycleId);
    
}
