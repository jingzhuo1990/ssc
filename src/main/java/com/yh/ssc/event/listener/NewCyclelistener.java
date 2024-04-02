package com.yh.ssc.event.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.yh.ssc.converter.DetailConverter;
import com.yh.ssc.converter.PlanConverter;
import com.yh.ssc.converter.SscDataConverter;
import com.yh.ssc.data.dataobject.Detail;
import com.yh.ssc.data.dataobject.Plan;
import com.yh.ssc.data.query.DetailQuery;
import com.yh.ssc.data.query.PlanQuery;
import com.yh.ssc.dto.DataContext;
import com.yh.ssc.data.dataobject.SscData;
import com.yh.ssc.dto.DetailDTO;
import com.yh.ssc.dto.PlanDTO;
import com.yh.ssc.dto.SscDataDTO;
import com.yh.ssc.enmus.DetailStateEnums;
import com.yh.ssc.enmus.IndexEnums;
import com.yh.ssc.enmus.PlanStateEnums;
import com.yh.ssc.event.NewCycleEvent;
import com.yh.ssc.event.publish.EventPublisher;
import com.yh.ssc.service.DataFactoryService;
import com.yh.ssc.service.orm.DetailOrmService;
import com.yh.ssc.service.orm.PlanOrmService;
import com.yh.ssc.utils.StreamUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class NewCyclelistener {
    
    @Resource
    private DataFactoryService dataFactoryService;
    @Resource
    private PlanOrmService planOrmService;
    @Resource
    private DetailOrmService detailOrmService;
    @Resource
    private EventPublisher eventPublisher;
    
    
    @EventListener(value = NewCycleEvent.class)
    public void plan(NewCycleEvent newCycleEvent) {
        SscDataDTO sscDataDTO = (SscDataDTO) newCycleEvent.getSource();
        List<DetailDTO> existDetails = existPlanDetail(sscDataDTO);
        if (CollectionUtils.isNotEmpty(existDetails)){
            existDetails.forEach(existDetail->{
                handlerPlanDetail(sscDataDTO,existDetail);
            });
        }else {
            addNewPlan(sscDataDTO);
        }
    }
    
    private void addNewPlan(SscDataDTO sscDataDTO){
        DataContext dataContext = dataFactoryService.buildDataContext(30);
        List<DataContext.SinglePercent> singlePercents = dataContext.getSinglePercents();
        
        boolean newPlan = false;
        
        List<List<Integer>> canditate = Lists.newArrayList();
        for (DataContext.SinglePercent singlePercent:singlePercents){
            IndexEnums indexEnums = singlePercent.getIndexEnums();
            List<Integer> candidateNum = singlePercent.getDistributeds().stream().filter(x->x.getCnt()==0).map(
                    DataContext.Distributed::getNum).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(candidateNum)){
                newPlan = true;
            }
            canditate.add(indexEnums.getIndex(),candidateNum);
            if (newPlan){
                doAddNewPlan(sscDataDTO.getCycleId(),sscDataDTO.getCycleValue(),canditate,indexEnums);
            }
        }
    }
    
    private List<DetailDTO> existPlanDetail(SscDataDTO sscDataDTO){
        Long cycleId =sscDataDTO.getLastData().getLastCycleId();
        String cycleValue =sscDataDTO.getLastData().getLastCycleValue();
        List<Detail> details = detailOrmService.listByCondition(DetailQuery.builder()
                .cycleValue(cycleValue)
                .state(DetailStateEnums.SEND_OK.getState())
                .build());
        
        return StreamUtils.ofNullable(details).map(DetailConverter::toDTO).collect(Collectors.toList());
    }
    
    private void handlerPlanDetail(SscDataDTO sscDataDTO, DetailDTO detail){
        Long planId = detail.getPlanId();
        List<Plan> plans = planOrmService.listByCondition(PlanQuery.builder().id(planId).build());
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(plans),"plan not exist");
        Plan plan = plans.get(0);
        PlanDTO planDTO = PlanConverter.toDTO(plan);
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
    
    private void doAddNewPlan(Long startCycleId,String startCycleValue,List<List<Integer>> candidate,IndexEnums indexEnums){
        Plan plan = new Plan();
        plan.setCreateTime(new Date());
        plan.setRound(10);
        plan.setCurrent(0);
        plan.setStartCycleId(startCycleId);
        plan.setStartCycleValue(startCycleValue);
        plan.setState(PlanStateEnums.RUNNING.getState());
        plan.setCandidate(JSONArray.toJSONString(candidate));
        plan.setCandidateInner(candidate);
        
        plan.setType(indexEnums.getType());
        plan.setSubType(indexEnums.getSubType());
        
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("1","1");
        jsonObject.put("2","1");
        jsonObject.put("3","1");
        jsonObject.put("4","1");
        jsonObject.put("5","2");
        jsonObject.put("6","2");
        jsonObject.put("7","2");
        jsonObject.put("9","2");
        jsonObject.put("9","2");
        jsonObject.put("10","4");
        
        plan.setPolicy(jsonObject.toJSONString());
        planOrmService.add(plan);
    }
}
