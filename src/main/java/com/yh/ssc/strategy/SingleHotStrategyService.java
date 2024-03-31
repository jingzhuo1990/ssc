package com.yh.ssc.strategy;

import com.google.common.collect.Lists;
import com.yh.ssc.dto.QueryData;
import com.yh.ssc.enmus.IndexEnums;
import com.yh.ssc.service.SscDataService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @program: mediAsst
 * @description:
 * @author: yehang
 * @create: 2024-03-30 00:24
 **/
@Component
public class SingleHotStrategyService implements BaseStrategyService{
    
    private Integer hisNum=10;
    
    @Resource
    private SscDataService sscDataRealService;
    
    
    @Override
    public List<List<String>> recommend(List<QueryData.ResultHis> resultHis) {
        QueryData queryData = sscDataRealService.query(190,30);
        List<String> wanTop5 = topHot(queryData, IndexEnums.WAN.getIndex(),5);
        List<String> qian = Lists.newArrayList();
        List<String> bai = Lists.newArrayList();
        List<String> shi = Lists.newArrayList();
        List<String> ge = Lists.newArrayList();
        List<List<String>> recommendRst = Lists.newArrayList();
        recommendRst.add(wanTop5);
        recommendRst.add(qian);
        recommendRst.add(bai);
        recommendRst.add(shi);
        recommendRst.add(ge);
        return recommendRst;
    }
    
    private List<String> topHot(QueryData queryData,Integer index,Integer topSize){
        List<String> data = queryData.getLottery_result_history().stream().map(x->x.getGame_result().get(index)).collect(Collectors.toList());
        List<String> topHot = data.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                // 按值降序排序
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                // 限制为前5个元素
                .limit(topSize)
                // 从条目中提取键
                .map(Map.Entry::getKey)
                // 收集结果
                .collect(Collectors.toList());
        return topHot;
    }
    
}
