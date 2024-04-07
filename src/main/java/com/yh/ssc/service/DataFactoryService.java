package com.yh.ssc.service;

import com.google.common.collect.Lists;
import com.yh.ssc.constants.Common;
import com.yh.ssc.dto.DataContext;
import com.yh.ssc.dto.QueryData;
import com.yh.ssc.enmus.HotStateEnums;
import com.yh.ssc.enmus.IndexEnums;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @program: mediAsst
 * @description:
 * @author: yehang
 * @create: 2024-03-30 19:51
 **/
@Service
public class DataFactoryService {
    
    @Resource
    private SscService sscRealService;
    
    public DataContext buildDataContext(Integer gameId,Integer row){
        DataContext dataContext = new DataContext();
        QueryData queryData = sscRealService.query(gameId,row);
        
        List<DataContext.SinglePercent> singlePercents = Lists.newArrayList();
        for (IndexEnums indexEnums : IndexEnums.values()){
            List<Integer> indexDatas = parseSingleInt(queryData, indexEnums.getIndex());
            singlePercents.add(buildProbabilities(indexDatas,indexEnums));
        }
        dataContext.setSinglePercents(singlePercents);
        
        
        return dataContext;
        
    }
    
    
    private DataContext.SinglePercent buildProbabilities(List<Integer> wanList,IndexEnums indexEnums) {
        DataContext.SinglePercent singlePercent = new DataContext.SinglePercent();
        
        singlePercent.setIndexEnums(indexEnums);
        List<DataContext.Distributed> distributeds = Lists.newArrayList();
        singlePercent.setDistributeds(distributeds);
        
        Map<Integer, Long> frequencyMap = wanList.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        
        // 创建并初始化一个包含0到9（包含）的整数的Map，计数初始值为0
        Map<Integer, Long> completeFrequencyMap = IntStream.rangeClosed(0, 9).boxed()
                .collect(Collectors.toMap(Function.identity(), key -> 0L));
        
        // 更新Map中的计数值
        frequencyMap.forEach((key, value) -> completeFrequencyMap.merge(key, value, Long::sum));
        
        for (Map.Entry<Integer, Long> entry : completeFrequencyMap.entrySet()) {
            
            final DataContext.Distributed distributed = getDistributed(entry);
            
            distributeds.add(distributed);
        }
        return singlePercent;
    }
    
    private static DataContext.Distributed getDistributed(Map.Entry<Integer, Long> entry) {
        DataContext.Distributed distributed = new DataContext.Distributed();
        distributed.setNum(entry.getKey());
        distributed.setCnt(entry.getValue().intValue());
        
        // 设置状态
        if (entry.getValue() >= 5) {
            distributed.setStateEnums(HotStateEnums.HOT);
        } else if (entry.getValue() == 4 || entry.getValue()==3) {
            distributed.setStateEnums(HotStateEnums.MID);
        } else {
            distributed.setStateEnums(HotStateEnums.COLD);
        }
        return distributed;
    }
    
    private List<Integer> parseSingleInt(QueryData queryData,Integer index){
        List<Integer> data = queryData.getLottery_result_history().stream().map(x->x.getGame_result().get(index)).map(Integer::new).collect(
                Collectors.toList());
        return data;
    }
    
    
    private List<String> parseSingle(QueryData queryData,Integer index){
        List<String> data = queryData.getLottery_result_history().stream().map(x->x.getGame_result().get(index)).collect(
                Collectors.toList());
        return data;
    }
    
    private List<String> topHot(QueryData queryData,Integer index,Integer topSize){
        List<String> data = parseSingle(queryData,index);
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
