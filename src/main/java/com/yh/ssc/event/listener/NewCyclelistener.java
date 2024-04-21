package com.yh.ssc.event.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.yh.ssc.conf.GameProperteries;
import com.yh.ssc.conf.SumContext;
import com.yh.ssc.constants.Common;
import com.yh.ssc.converter.DetailConverter;
import com.yh.ssc.converter.PlanConverter;
import com.yh.ssc.data.dataobject.Detail;
import com.yh.ssc.data.dataobject.Plan;
import com.yh.ssc.data.query.DetailQuery;
import com.yh.ssc.data.query.PlanQuery;
import com.yh.ssc.data.query.SscDataQuery;
import com.yh.ssc.dto.DataContext;
import com.yh.ssc.dto.DetailDTO;
import com.yh.ssc.dto.PlanDTO;
import com.yh.ssc.dto.QueryData;
import com.yh.ssc.dto.SscDataDTO;
import com.yh.ssc.enmus.DetailStateEnums;
import com.yh.ssc.enmus.IndexEnums;
import com.yh.ssc.enmus.PlanStateEnums;
import com.yh.ssc.event.NewCycleEvent;
import com.yh.ssc.event.publish.EventPublisher;
import com.yh.ssc.profit.Profit;
import com.yh.ssc.profit.ProfitProperties;
import com.yh.ssc.service.DataFactoryService;
import com.yh.ssc.service.SscService;
import com.yh.ssc.service.orm.DetailOrmService;
import com.yh.ssc.service.orm.PlanOrmService;
import com.yh.ssc.service.orm.SscDataOrmService;
import com.yh.ssc.utils.DataUtils;
import com.yh.ssc.utils.StreamUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-03-31 13:00
 **/
@Service
@Slf4j
public class NewCyclelistener {
    
    @Resource
    private DataFactoryService dataFactoryService;
    @Resource
    private PlanOrmService planOrmService;
    @Resource
    private DetailOrmService detailOrmService;
    @Resource
    private EventPublisher eventPublisher;
    @Resource
    private ProfitProperties profitProperties;
    @Resource
    private GameProperteries gameProperteries;
    @Resource
    private SscService sscRealService;
    @Resource
    private SscDataOrmService sscDataOrmService;
    @Resource
    private SumContext sumContext;
    
    
    @EventListener(value = NewCycleEvent.class)
    public void plan(NewCycleEvent newCycleEvent) {
        try {
            log.warn("new newCycleEvent:{}",newCycleEvent);
            SscDataDTO sscDataDTO = (SscDataDTO) newCycleEvent.getSource();
            if (!gameProperteries.allowPlan(sscDataDTO.getGameId().intValue())){
                log.warn("game :{} not to plan and direct return",sscDataDTO.getGameId());
                return;
            }
            log.info("try to plan,gameId:{}",sscDataDTO.getGameId());
            Long lastCycleId = sscDataDTO.getLastData().getLastCycleId();
            if (lastCycleId==null){
                log.warn("lastCycleId is null ,return,:{}",sscDataDTO);
            }
            List<DetailDTO> existDetails = existPlanDetail(sscDataDTO);
            if (CollectionUtils.isNotEmpty(existDetails)){
                existDetails.forEach(existDetail->{
                    handlerPlanDetail(sscDataDTO,existDetail);
                });
            }
//            addNewPlan(sscDataDTO);
            last2sum(newCycleEvent);
        }catch (Exception e){
            log.error("NewCycleEvent plan handler error,:{}",e);
        }
        
    }
    
//    @EventListener(value = NewCycleEvent.class)
    public void last2sum(NewCycleEvent newCycleEvent) {
        SscDataDTO sscDataDTO = (SscDataDTO) newCycleEvent.getSource();
        if (sscDataDTO.getGameId()!=190){
            return;
        }
        log.info("start last2sum");
        List<SscDataDTO> sscDataDTOS = sscDataOrmService.listByCondition(
                SscDataQuery.builder().gameId(190L).limit(20).build());
        
        sscDataDTOS = sscDataDTOS.stream().filter(x->x.getNowData()!=null && x.getNowData().getWan()!=null).collect(
                Collectors.toList());
        
        List<String> cycleValues = sscDataDTOS.stream().map(SscDataDTO::getCycleValue).collect(Collectors.toList());
        /*if (!DataUtils.isSequentiallyIncremented(cycleValues)){
            log.warn("not valid cycle,data:{}",JSON.toJSONString(cycleValues));
            return;
        }*/
        List<Integer> ca = sscDataDTOS.stream().map(x->x.getNowData().getShi()+x.getNowData().getGe()).collect(Collectors.toList());
        
        //超过7期
        Integer exist = ca.subList(0,7).stream().filter(x-> sumContext.contains(x)).findAny().orElse(null);
        if (exist==null){
            log.warn("passed 7 time return");
            return;
        }
        
        exist = ca.subList(0,4).stream().filter(x->sumContext.contains(x)).findAny().orElse(null);
        if (exist!=null){
            return;
        }
        
        addLast2sumPlan(sscDataDTO);
    }
    
    private void existplan(SscDataDTO sscDataDTO){
        String cycleVal = sscDataDTO.getNowData().getLastCycleValue();
        List<String> cycleValue = new ArrayList<>();
        cycleValue.add(cycleVal);
        cycleValue.add(String.valueOf(Long.valueOf(cycleVal)-1));
        List<Plan> plans = planOrmService.listByCondition(PlanQuery.builder().gameId(190L).type("SUM").subType("LAST2").cycleValues(cycleValue).build());
        if (CollectionUtils.isNotEmpty(plans)){
        
        }
    }
    
    
    private void addLast2sumPlan(SscDataDTO sscDataDTO){
        List<String> cycleVal = new ArrayList<>();
        String curCycleVal = sscDataDTO.getCycleValue();
        List<Plan> exists = planOrmService.listByCondition(PlanQuery.builder().gameId(190L).cycleValues(cycleVal).type("SUM").build());
        
        if (CollectionUtils.isNotEmpty(exists)){
            Long cycleValueNow = Long.valueOf(sscDataDTO.getCycleValue());
            Long startCycle = Long.valueOf(exists.get(0).getStartCycleValue());
            if (cycleValueNow-startCycle<=4){
                log.warn("already exist plan,id:{}",exists.get(0).getId());
                return;
            }
        }
        
        Plan plan = new Plan();
        plan.setGameId(sscDataDTO.getGameId());
        plan.setCreateTime(new Date());
        plan.setCandidate("[[6,7,8,9,10]]");
        
        plan.setCurrent(1);
        plan.setType("SUM");
        plan.setSubType("LAST2");
        plan.setState(PlanStateEnums.RUNNING.getState());
        plan.setStartCycleId(sscDataDTO.getCycleId());
        plan.setStartCycleValue(sscDataDTO.getCycleValue());
        
        List<Profit> profits = profitProperties.getProfits();
        JSONObject jsonObject = new JSONObject();
        for (Profit profit : profits) {
            jsonObject.put(String.valueOf(profit.getRound()),profit.getMultify());
        }
        plan.setRound(profits.size());
        plan.setPolicy(jsonObject.toJSONString());
        
        log.info("add last2sum plan");
        planOrmService.add(plan);
        
    }
    
    private void addNewPlan(SscDataDTO sscDataDTO){
        DataContext dataContext = dataFactoryService.buildDataContext(Math.toIntExact(sscDataDTO.getGameId()),sscDataDTO.getRow());
        List<DataContext.SinglePercent> singlePercents = dataContext.getSinglePercents();
        
        //每一位都需要单独看
        for (DataContext.SinglePercent singlePercent:singlePercents){
            IndexEnums indexEnums = singlePercent.getIndexEnums();
            List<Integer> candidateNum = singlePercent.getDistributeds().stream().filter(x->x.getCnt()==0).map(
                    DataContext.Distributed::getNum).collect(Collectors.toList());
            
            //需要拆分开，单独计划
            for (Integer candi:candidateNum){
                List<List<Integer>> canditate = new ArrayList<>();
                for (int i=0;i<4;i++){
                    canditate.add(i, new ArrayList<>());
                }
                canditate.add(indexEnums.getIndex(),Lists.newArrayList(candi));
                doAddNewPlan(sscDataDTO.getGameId(),sscDataDTO.getCycleId(),sscDataDTO.getCycleValue(),canditate,indexEnums);
            }
        }
    }
    
    private List<DetailDTO> existPlanDetail(SscDataDTO sscDataDTO){
        String cycleValue =sscDataDTO.getLastData().getLastCycleValue();
        List<Detail> details = detailOrmService.listByCondition(DetailQuery.builder()
                .gameId(sscDataDTO.getGameId())
                .cycleValue(cycleValue)
                .state(DetailStateEnums.SEND_OK.getState())
                .build());
        return StreamUtils.ofNullable(details).map(DetailConverter::toDTO).collect(Collectors.toList());
    }
    
    private boolean isSum(Long planId){
        Plan plan = planOrmService.getById(planId);
        return plan.getType().equals("SUM") ?true:false;
    }
    
    private void handlerPlanDetail(SscDataDTO sscDataDTO, DetailDTO detail){
        Long planId = detail.getPlanId();
        List<Plan> plans = planOrmService.listByCondition(PlanQuery.builder().id(planId).build());
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(plans),"plan not exist");
        Plan plan = plans.get(0);
        PlanDTO planDTO = PlanConverter.toDTO(plan);
        if (isSum(detail.getPlanId())){
            if (sumContext.contains(sscDataDTO.getLastSum())){
                eventPublisher.publishPlanSuccess(planDTO,detail);
            }else {
                eventPublisher.publishDetailFail(sscDataDTO,planDTO,detail);
            }
        }else {
            IndexEnums indexEnums = IndexEnums.getBySubType(planDTO.getSubType());
            List<List<Integer>> listOfLists = JSON.parseObject(planDTO.getCandidate(), new TypeReference<List<List<Integer>>>() {});
            List<Integer> singleCandidate = listOfLists.get(indexEnums.getIndex());
            Integer index = indexEnums.getIndex();
            Integer singleResult = sscDataDTO.getByIndex(index);
            if (singleCandidate.contains(singleResult)){
                eventPublisher.publishPlanSuccess(planDTO,detail);
            }else {
                eventPublisher.publishDetailFail(sscDataDTO,planDTO,detail);
            }
        }
    }
    
    private boolean exitsPlan(Long gameId,IndexEnums indexEnums,Integer singleCan,String startCycleValue){
        List<String> cycleValues = new ArrayList<>();
        for (int i= 1 ; i< Common.LAST_HIS;i++){
            Long temp = Long.valueOf(startCycleValue)-i;
            cycleValues.add(String.valueOf(temp));
        }
        List<Plan> explans = planOrmService.listByCondition(PlanQuery.builder().gameId(gameId).subType(indexEnums.getSubType()).cycleValues(cycleValues).singleCan(singleCan).build());
        if (CollectionUtils.isNotEmpty(explans)){
            return true;
        }
        return false;
    }
    
    private void doAddNewPlan(Long gameId,Long startCycleId,String startCycleValue,List<List<Integer>> candidates,IndexEnums indexEnums){
        Integer singleCan = candidates.get(indexEnums.getIndex()).get(0);
        
        if (exitsPlan(gameId,indexEnums,singleCan,startCycleValue)){
            log.info("existed plan");
            return;
        }
        Plan plan = new Plan();
        plan.setGameId(gameId);
        plan.setCreateTime(new Date());
        plan.setRound(profitProperties.getProfits().size());
        plan.setCurrent(0);
        plan.setStartCycleId(startCycleId);
        plan.setStartCycleValue(startCycleValue);
        plan.setState(PlanStateEnums.RUNNING.getState());
        plan.setCandidate(JSONArray.toJSONString(candidates));
        plan.setCandidateInner(candidates);
        
        plan.setType(indexEnums.getType());
        plan.setSubType(indexEnums.getSubType());
        plan.setSingleCan(singleCan);
        
        List<Profit> profits = profitProperties.getProfits();
        JSONObject jsonObject = new JSONObject();
        for (Profit profit : profits) {
            jsonObject.put(String.valueOf(profit.getRound()),profit.getMultify());
        }
        
        plan.setPolicy(jsonObject.toJSONString());
        log.info("new plan,plan:{}",plan);
        
        planOrmService.add(plan);
    }
}
