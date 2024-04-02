package com.yh.ssc.event.listener;

import com.alibaba.fastjson.JSONObject;
import com.yh.ssc.constants.Common;
import com.yh.ssc.converter.DetailConverter;
import com.yh.ssc.converter.PlanConverter;
import com.yh.ssc.data.dataobject.Detail;
import com.yh.ssc.data.dataobject.Plan;
import com.yh.ssc.dto.DetailDTO;
import com.yh.ssc.dto.PlanDTO;
import com.yh.ssc.dto.SscDataDTO;
import com.yh.ssc.enmus.DetailStateEnums;
import com.yh.ssc.enmus.PlanStateEnums;
import com.yh.ssc.event.NewPlanEvent;
import com.yh.ssc.event.PlanDetailFailedEvent;
import com.yh.ssc.event.PlanSuccessEvent;
import com.yh.ssc.service.SscDataService;
import com.yh.ssc.service.orm.DetailOrmService;
import com.yh.ssc.service.orm.PlanOrmService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-04-01 15:04
 **/

@Component
public class PlanDetailListener {
    
    @Resource
    private DetailOrmService detailOrmService;
    
    @Resource
    private SscDataService sscDataRealService;
    @Resource
    private PlanOrmService planOrmService;
    @EventListener(value = NewPlanEvent.class)
    public void execute(PlanDTO plan){
        Detail detail = new Detail();
        
        detail.setPlanId(plan.getId());
        String policy = plan.getPolicy();
        
        detail.setCreateTime(new Date());
        detail.setAmount(2);
        Integer round = 1;
        detail.setRound(round);
        JSONObject policyJSON = JSONObject.parseObject(policy);
        Integer multipy = policyJSON.getInteger(String.valueOf(round));
        Integer amount = multipy * Common.SINGLE_AMOUNT;
        detail.setAmount(amount);
        
        detail.setState(DetailStateEnums.RUNNING.getState());
        detail.setCycleId(plan.getStartCycleId());
        detail.setCycleValue(plan.getStartCycleValue());
        
        detailOrmService.add(detail);
    }
    
    @EventListener(value = PlanSuccessEvent.class)
    public void planSuccess(PlanSuccessEvent planSuccessEvent){
        PlanDTO planDTO = (PlanDTO) planSuccessEvent.getSource();
        DetailDTO detailDTO = planSuccessEvent.getDetail();
        planDTO.setState(PlanStateEnums.SUCCESS.getState());
        Plan plan = PlanConverter.toDO(planDTO);
        planOrmService.update(plan);
        
        detailDTO.setState(DetailStateEnums.SUCCESS.getState());
        Detail detail = DetailConverter.toDO(detailDTO);
        detailOrmService.update(detail);
    }
    
    @EventListener(value = PlanDetailFailedEvent.class)
    public void planFailed(PlanDetailFailedEvent failedEvent){
        PlanDTO planDTO = (PlanDTO) failedEvent.getSource();
        DetailDTO detailDTO = failedEvent.getDetail();
        SscDataDTO sscDataDTO = failedEvent.getSscDataDTO();
        Long currentCycleId = sscDataDTO.getCycleId();
        String currentCycleValue = sscDataDTO.getCycleValue();
        detailDTO.setState(DetailStateEnums.FAILED.getState());
        Detail detail = DetailConverter.toDO(detailDTO);
        detailOrmService.update(detail);
        
        Integer round = planDTO.getRound();
        Integer current = detailDTO.getRound();
        //整体失败
        if (round<=current){
            planDTO.setState(PlanStateEnums.FAILED.getState());
            Plan plan = PlanConverter.toDO(planDTO);
            planOrmService.update(plan);
        } else {
            Detail newDetail = buildNewDetail(detailDTO,planDTO,currentCycleId,currentCycleValue);
            detailOrmService.add(newDetail);
        }
        
    }
    
    
    private Detail buildNewDetail(DetailDTO oldDetail,PlanDTO planDTO,Long currentCycleId,String currentCycleValue){
        Integer round = oldDetail.getRound();
        if (round>=planDTO.getRound()){
            throw new IllegalArgumentException("round expend");
        }
        Detail newDetail = new Detail();
        newDetail.setState(DetailStateEnums.RUNNING.getState());
        newDetail.setPlanId(planDTO.getId());
        
        Integer multify = planDTO.getPolicyJSON().getInteger(String.valueOf(round));
        Integer amount = multify* Common.SINGLE_AMOUNT;
        
        newDetail.setRound(round+1);
        newDetail.setAmount(amount);
        newDetail.setCycleId(currentCycleId);
        newDetail.setCycleValue(currentCycleValue);
        newDetail.setCreateTime(new Date());
        return newDetail;
    }
    
}
