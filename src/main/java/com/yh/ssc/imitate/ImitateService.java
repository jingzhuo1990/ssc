package com.yh.ssc.imitate;

import com.google.common.util.concurrent.AtomicDouble;
import com.yh.ssc.data.query.SscDataQuery;
import com.yh.ssc.dto.SscDataDTO;
import com.yh.ssc.profit.Profit;
import com.yh.ssc.profit.ProfitService;
import com.yh.ssc.service.orm.SscDataOrmService;
import com.yh.ssc.service.result.SscDataResultService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-04-03 14:27
 **/
@Component
@Slf4j
public class ImitateService {
    
    @Resource
    private ProfitService profitService;
    @Resource
    private SscDataResultService sscDataResultService;
    @Resource
    private SscDataOrmService sscDataOrmService;
    
    private static AtomicDouble mostCost = new AtomicDouble(0);
    
    private static AtomicInteger totalPlan = new AtomicInteger(0);
    
    public double imitate(Integer gameId,Integer cnt,Long startId,Long endId){
        mostCost.set(0);
        totalPlan.set(0);
        Map<String, Map<Integer, List<Integer>>> distribute = sscDataResultService.distribution(gameId,cnt,startId,endId);
        
        double allProfit = 0;
        
        // 开始遍历
        for (Map.Entry<String, Map<Integer, List<Integer>>> outerEntry : distribute.entrySet()) {
            String outerKey = outerEntry.getKey(); // 外层Map的Key
            Map<Integer, List<Integer>> innerMap = outerEntry.getValue(); // 内层Map
            
            for (Map.Entry<Integer, List<Integer>> innerEntry : innerMap.entrySet()) {
                Integer innerKey = innerEntry.getKey(); // 内层Map的Key
                List<Integer> integerList = innerEntry.getValue(); // List<Integer>
                
                // 遍历List<Integer>
                for (Integer number : integerList) {
                    totalPlan.addAndGet(1);
                    // 这里是对List<Integer>中每个Integer的操作
                    allProfit += calcuSingle(number-cnt+1);
                }
            }
        }
        log.info("gameId:{},startId:{},endId:{},gapRound:{},planCnt:{},round:{},mostCost:{}",gameId,startId,endId,cnt,totalPlan,profitService.getProfitsMap().size(),mostCost);
        return allProfit;
        
    }
    
    private double calcuSingle(Integer number){
        Map<Integer, Profit> profitMap = profitService.getProfitsMap();
        double allCost = profitMap.values().stream().map(Profit::getCost).mapToDouble(Double::doubleValue).sum();
        if (profitMap.containsKey(number)){
            return profitMap.get(number).getProfit();
        }
        mostCost.addAndGet(allCost);
        return -allCost;
    }
    
    public Double calLastProfit(Integer gameId,Integer cnt,Long startId,Long endId,Integer start,Integer end){
        List<SscDataDTO> sscDataDTOS = sscDataOrmService.listByCondition(
                SscDataQuery.builder().gameId(Long.valueOf(gameId)).startId(startId).endId(endId).build());
        sscDataDTOS = sscDataDTOS.stream().filter(x->x.getNowData()!=null && x.getNowData().getWan()!=null).collect(
                Collectors.toList());
//        sscDataDTOS.sort(Comparator.comparing(SscDataDTO::getCycleValue));
        
        List<Integer> targetNumbers = new ArrayList();
        for (;start<=end;start++){
            targetNumbers.add(start);
        }
        Double totalProfit = 0.0;
        
        Integer lastNot = 0;
        for (Integer i = 0; i < sscDataDTOS.size() - cnt - 2; i++) {
            List<SscDataDTO> temp = sscDataDTOS.subList(i, i + cnt);
            boolean needPlan = needPlan(temp, targetNumbers);
            if (needPlan) {
                if (i-lastNot!=1){
                    List<SscDataDTO> candi = sscDataDTOS.subList(i + cnt, i + cnt + 2);
                    ProInter profit = calPlan(candi, targetNumbers);
                    totalProfit += profit.getProfit();
                    i = i + profit.getIndex();
                    if (profit.getProfit() < 0) {
                        lastNot = i;
                    }
                }else {
                    lastNot = i;
                }
                
            }
        }
        return totalProfit;
    }
    @Data
    public static class ProInter{
        private Double profit;
        private Integer index;
    }
    
    private ProInter calPlan(List<SscDataDTO> sscDataDTOS,List<Integer> target){
        ProInter proInter = new ProInter();
        for (int i=0;i<sscDataDTOS.size();i++){
            SscDataDTO sscDataDTO = sscDataDTOS.get(i);
            Integer ta = sscDataDTO.getNowData().getShi() + sscDataDTO.getNowData().getGe();
            if (target.contains(ta)){
                proInter.setIndex(i);
                if (i==0){
                    proInter.setProfit(Double.valueOf(196-43*2));
                }else if (i==1){
                    proInter.setProfit(Double.valueOf(196-43*4));
                }
                return proInter;
            }
        }
        proInter.setIndex(3);
        proInter.setProfit(Double.valueOf(-43*4));
        return proInter;
    }
    
    private boolean needPlan(List<SscDataDTO> sscDataDTO ,List<Integer> targetNumbers){
        List<Integer> nums = sscDataDTO
                .stream()
                .map(x-> x.getNowData().getShi()+x.getNowData().getGe())
                .collect(Collectors.toList());
        
        Integer exist = nums.stream().filter(x->targetNumbers.contains(x)).findAny().orElse(null);
        return exist==null?true:false;
    }
    
    
    
    public List<Integer> imitateLast2sum(Integer gameId,Integer cnt,Long startId,Long endId,Integer start,Integer end){
        List<SscDataDTO> sscDataDTOS = sscDataOrmService.listByCondition(
                SscDataQuery.builder().gameId(Long.valueOf(gameId)).startId(startId).endId(endId).build());
        sscDataDTOS = sscDataDTOS.stream().filter(x->x.getNowData()!=null && x.getNowData().getWan()!=null).collect(
                Collectors.toList());
        sscDataDTOS.sort(Comparator.comparing(SscDataDTO::getCycleValue));
        
        List<Integer> nums = sscDataDTOS
                .stream()
                .map(x-> x.getNowData().getShi()+x.getNowData().getGe())
                .collect(Collectors.toList());
        
        List<Integer> targetNumbers = new ArrayList();
        for (;start<=end;start++){
            targetNumbers.add(start);
        }
        
        // Indexes where target numbers appear
        List<Integer> indexes = new ArrayList<>();
        
        // Iterate over the data list and populate the indexes list
        for (int i = 0; i < nums.size(); i++) {
            if (targetNumbers.contains(nums.get(i))) {
                indexes.add(i);
            }
        }
        
        // Calculate the distances
        List<Integer> distances = new ArrayList<>();
        for (int i = 1; i < indexes.size(); i++) {
            distances.add(indexes.get(i) - indexes.get(i - 1) - 1);
        }
        distances = distances.stream().filter(x->x.intValue()>=cnt).collect(Collectors.toList());
        
        return distances;
        
    }
    
    public Map<String,HotCandidate> imitateHot(Integer gameId,Integer cnt,Long startId,Long endId,Double min,int hotNum){
        List<SscDataDTO> sscDataDTOS = sscDataOrmService.listByCondition(
                SscDataQuery.builder().gameId(Long.valueOf(gameId)).startId(startId).endId(endId).build());
        sscDataDTOS = sscDataDTOS.stream().filter(x->x.getNowData()!=null && x.getNowData().getWan()!=null).collect(
                Collectors.toList());
        sscDataDTOS.sort(Comparator.comparing(SscDataDTO::getCycleValue));
        
        Double total = 0.0;
        
        Map<String,HotCandidate> map = new HashMap<>();
        for (int i=0;i<sscDataDTOS.size()-cnt;i++){
            List<SscDataDTO> tempData = sscDataDTOS.subList(i,i+cnt);
            
            HotCandidate hotCandidate = parseHot(tempData,hotNum);
            if (hotCandidate.getTotalChance().compareTo(min)>=0){
                SscDataDTO rst = sscDataDTOS.get(i+cnt);
                Double current = profit(hotCandidate.getCandidate(),rst,hotNum);
                total += current;
                hotCandidate.setProfit(current);
                map.put(rst.getCycleValue(),hotCandidate);
            }
        }
        
        List<Map.Entry<String, HotCandidate>> sortedEntries = map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toList());
        
        //如果你想得到一个已排序的Map，可以进一步操作：
        LinkedHashMap<String, HotCandidate> sortedMap = sortedEntries.stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new
                ));
        
        //填充值
        Double previousProfit = 0.0;
        Double minProfit=10000.0;
        Double maxProfit=0.0;
        Double current = 1000.0;
        for (HotCandidate candidate : sortedMap.values()) {
            // 更新当前HotCandidate的current值，减去previousProfit
            candidate.setCurrent(current + previousProfit);
            current = candidate.getCurrent();
            // 更新totalProfit，加上当前HotCandidate的profit
            previousProfit = candidate.getProfit();
            candidate.setTotalProfit(candidate.getTotalProfit() + candidate.getProfit());
            if (candidate.getCurrent()>maxProfit){
                maxProfit = candidate.getCurrent();
            }
            if (candidate.getCurrent()<minProfit){
                minProfit = candidate.getCurrent();
            }
        }
        log.info("imitate host:minProfit:{}",minProfit);
        log.info("imitate host:maxProfit:{}",maxProfit);
        log.info("imitate host:totalProfit:{}",total);
        return sortedMap;
    }
    
    @Data
    public static class HotCandidate{
        private Double profit;
        
        List<Map.Entry<String, Map<String, Object>>> candidate;
        
        private Double totalChance;
        
        private Double totalProfit = 0.0;
        
        private Double current = 1000.0;
    }
    
    private Double profit(List<Map.Entry<String, Map<String, Object>>> candidate,SscDataDTO rst,Integer hotNum){
        AtomicReference<Integer> totalMatch = new AtomicReference<>(0);
        candidate.stream().forEach(integerLongEntry -> {
            Integer matchCnt = matchCnt(rst, Integer.valueOf(integerLongEntry.getKey()));
            totalMatch.updateAndGet(v -> v + matchCnt);
        });
        return calProfit(totalMatch.get(),hotNum);
    }
    
    private Double calProfit(Integer matchCnt,Integer hotNum){
        Double cost = 10.0*hotNum;
        Double profit = matchCnt * 19.6;
        return profit-cost;
    }
    
    private Integer matchCnt(SscDataDTO rst,Integer candidate){
        Integer matchCnt = 0;
        if (rst.getNowData().getWan().intValue()==candidate.intValue()){
            matchCnt+=1;
        }
        if (rst.getNowData().getQian().intValue()==candidate.intValue()){
            matchCnt+=1;
        }
        if (rst.getNowData().getBai().intValue()==candidate.intValue()){
            matchCnt+=1;
        }
        if (rst.getNowData().getShi().intValue()==candidate.intValue()){
            matchCnt+=1;
        }
        if (rst.getNowData().getGe().intValue()==candidate.intValue()){
            matchCnt+=1;
        }
        return matchCnt;
    }
    
    private HotCandidate parseHot(List<SscDataDTO> sscDataDTOS,Integer hotNum){
        List<Integer> nums = new ArrayList<>();
        sscDataDTOS.stream().forEach(x->{
            nums.add(x.getNowData().getWan().intValue());
            nums.add(x.getNowData().getQian().intValue());
            nums.add(x.getNowData().getBai().intValue());
            nums.add(x.getNowData().getShi().intValue());
            nums.add(x.getNowData().getGe().intValue());
        });
        
        // 使用Stream API来统计每个数字的出现次数并排序
        Map<Integer, Long> frequencyMap = nums.stream()
                .collect(Collectors.groupingBy(n -> n, Collectors.counting()));
        
        // 转换为一个包含次数和概率的Map
        Map<String, Map<String, Object>> resultMap = new HashMap<>();
        
        final int totalNumbers = sscDataDTOS.size() * 5;
        frequencyMap.forEach((k, v) -> {
            Map<String, Object> detailsMap = new HashMap<>();
            detailsMap.put("count", v);
            double probability = v / (double) totalNumbers;
            detailsMap.put("probability", probability);
            resultMap.put(String.valueOf(k), detailsMap);
        });
        
        // 将resultMap转换为List<Map.Entry<Integer, Map<String, Object>>>，方便排序
        List<Map.Entry<String, Map<String, Object>>> sortedEntries = resultMap.entrySet()
                .stream()
                // 按计数（count）降序排序
                .sorted((entry1, entry2) -> ((Long)entry2.getValue().get("count")).compareTo((Long)entry1.getValue().get("count")))
                // 获取前三名
                .limit(hotNum)
                .collect(Collectors.toList());
        
        // 计算前三名的count之和
        double topProbaby = sortedEntries.stream()
                .mapToDouble(entry -> (Double) entry.getValue().get("probability"))
                .sum();
        
        HotCandidate hotCandidate = new HotCandidate();
        hotCandidate.setCandidate(sortedEntries);
        hotCandidate.setTotalChance(topProbaby);
        return hotCandidate;
    }
    
}
