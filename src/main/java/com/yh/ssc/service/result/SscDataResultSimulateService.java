package com.yh.ssc.service.result;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.yh.ssc.dto.SscDataDTO;
import com.yh.ssc.data.query.SscDataQuery;
import com.yh.ssc.enmus.IndexEnums;
import com.yh.ssc.service.orm.SscDataOrmService;
import com.yh.ssc.utils.StreamUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-03-31 14:39
 **/
@Service
public class SscDataResultSimulateService implements SscDataResultService{
    
    @Resource
    private SscDataOrmService sscDataOrmService;
    
    @Override
    public List<Integer> query(Long gameId, Long cycleId) {
        List<Integer> rst = Lists.newArrayList();
        List<SscDataDTO> sscDataDTOS = sscDataOrmService.listByCondition(SscDataQuery.builder().gameId(gameId).cycleId(cycleId).build());
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(sscDataDTOS),"query data result by simulated is empty");
        SscDataDTO sscDataDTO = sscDataDTOS.get(0);
        rst.add(sscDataDTO.getLastData().getWan());
        rst.add(sscDataDTO.getLastData().getQian());
        rst.add(sscDataDTO.getLastData().getBai());
        rst.add(sscDataDTO.getLastData().getShi());
        rst.add(sscDataDTO.getLastData().getGe());
        return rst;
    }
    
    @Override
    public Map<String, Map<Integer, List<Integer>>> distribution(Integer gameId, Integer cnt,Long startId,Long endId) {
        Map<String, Map<Integer, List<Integer>>> interval = new HashMap<>();
        
        List<SscDataDTO> sscDataDTOS = sscDataOrmService.listByCondition(SscDataQuery.builder().startId(startId).endId(endId).build());
        sscDataDTOS = sscDataDTOS.stream().filter(x->x.getNowData()!=null && x.getNowData().getWan()!=null).collect(Collectors.toList());
        sscDataDTOS.sort(Comparator.comparing(SscDataDTO::getCycleValue));
        List<Integer> wan = StreamUtils.ofNullable(sscDataDTOS).map(x->x.getNowData().getWan()).collect(Collectors.toList());
        interval.put(IndexEnums.WAN.getSubType(),parseDistribution(wan,cnt));
        
        List<Integer> qian = StreamUtils.ofNullable(sscDataDTOS).map(x->x.getNowData().getQian()).collect(Collectors.toList());
        interval.put(IndexEnums.QIAN.getSubType(),parseDistribution(qian,cnt));
        
        List<Integer> bai = StreamUtils.ofNullable(sscDataDTOS).map(x->x.getNowData().getBai()).collect(Collectors.toList());
        interval.put(IndexEnums.BAI.getSubType(), parseDistribution(bai,cnt));
        
        List<Integer> shi = StreamUtils.ofNullable(sscDataDTOS).map(x->x.getNowData().getShi()).collect(Collectors.toList());
        interval.put(IndexEnums.SHI.getSubType(),parseDistribution(shi,cnt));
        
        List<Integer> ge = StreamUtils.ofNullable(sscDataDTOS).map(x->x.getNowData().getGe()).collect(Collectors.toList());
        interval.put(IndexEnums.GE.getSubType(), parseDistribution(ge,cnt));
        
        return interval;
    }
    
    @Override
    public void baozi(Integer gameId, Long startId, Long endId) {
        List<SscDataDTO> sscDataDTOS = sscDataOrmService.listByCondition(SscDataQuery.builder().startId(startId).endId(endId).build());
        sscDataDTOS = sscDataDTOS.stream().filter(x->x.getNowData()!=null && x.getNowData().getWan()!=null).collect(Collectors.toList());
        
    }
    
    private Map<Integer,List<Integer>> parseDistribution(List<Integer> data,Integer cnt){
        // 使用Map来存储每个数字的间隔统计
        Map<Integer,List<Integer>> interval = new HashMap<>();
        
        // 使用Map来存储每个数字最后一次出现的索引
        Map<Integer, Integer> lastIndexMap = new HashMap<>();
        
        // 遍历列表中的数字
        for (int i = 0; i < data.size(); i++) {
            Integer number = data.get(i);
            // 检查数字是否之前出现过
            if (lastIndexMap.containsKey(number)) {
                // 如果出现过，则计算间隔并存储或更新间隔统计Map
                int gap = i - lastIndexMap.get(number) - 1; // 减去1得到实际间隔
                if (cnt!=null && gap>=cnt){
                    if (interval.containsKey(number)){
                        interval.get(number).add(gap);
                    }else {
                        interval.put(number,Lists.newArrayList(gap));
                    }
                }
            }
            // 存储或更新这个数字的最后一次出现索引
            lastIndexMap.put(number, i);
        }
        return interval;
    }
    
    public static void main(String[] args) {
        
        SscDataResultSimulateService simulateService = new SscDataResultSimulateService();
        List<Integer> data = Lists.newArrayList(1, 8, 2, 3, 4, 4, 2, 1, 9, 3, 1, 2, 3, 4, 5, 6, 2, 1, 2, 8, 7);
        System.out.println(JSONObject.toJSONString(simulateService.parseDistribution(data,2)));
    }
    
}
