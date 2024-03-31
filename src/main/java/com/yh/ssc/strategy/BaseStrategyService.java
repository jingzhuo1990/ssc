package com.yh.ssc.strategy;

import com.yh.ssc.dto.QueryData;

import java.util.List;

public interface BaseStrategyService {

    List<List<String>> recommend(List<QueryData.ResultHis> resultHis);
}
