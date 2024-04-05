package com.yh.ssc.event.listener;

import com.alibaba.fastjson.JSONObject;
import com.yh.ssc.constants.Common;
import com.yh.ssc.data.dataobject.Detail;
import com.yh.ssc.dto.PlanDTO;
import com.yh.ssc.enmus.DetailStateEnums;
import com.yh.ssc.event.NewPlanEvent;
import com.yh.ssc.service.SscService;
import com.yh.ssc.service.orm.DetailOrmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-04-02 12:39
 **/
@Component
@Slf4j
public class NewPlanListener {
    
    @Resource
    private SscService sscRealService;
    @Resource
    private DetailOrmService detailOrmService;
    
    @EventListener(value = NewPlanEvent.class)
    public void execute(NewPlanEvent newPlanEvent){
        try {
            log.info("new plan event:{}",newPlanEvent);
            PlanDTO planDTO = (PlanDTO) newPlanEvent.getSource();
            Detail detail = new Detail();
            
            detail.setPlanId(planDTO.getId());
            String policy = planDTO.getPolicy();
            
            detail.setCreateTime(new Date());
            Integer round = 1;
            detail.setRound(round);
            JSONObject policyJSON = JSONObject.parseObject(policy);
            Integer multipy = policyJSON.getInteger(String.valueOf(round));
            Integer amount = multipy * Common.SINGLE_AMOUNT;
            detail.setAmount(amount);
            detail.setMultify(multipy);
            
            detail.setState(DetailStateEnums.RUNNING.getState());
            detail.setCycleId(planDTO.getStartCycleId());
            detail.setCycleValue(planDTO.getStartCycleValue());
            
            detailOrmService.add(detail);
        }catch(Exception e){
            log.error("NewPlanListener handler failed,{}",e);
        }
        
    }
}
