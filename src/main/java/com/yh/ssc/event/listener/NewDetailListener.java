package com.yh.ssc.event.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.yh.ssc.constants.Common;
import com.yh.ssc.converter.DetailConverter;
import com.yh.ssc.converter.PlanConverter;
import com.yh.ssc.data.dataobject.Detail;
import com.yh.ssc.data.dataobject.Plan;
import com.yh.ssc.data.query.DetailQuery;
import com.yh.ssc.data.query.PlanQuery;
import com.yh.ssc.dto.DetailDTO;
import com.yh.ssc.dto.PlanDTO;
import com.yh.ssc.dto.SscDataDTO;
import com.yh.ssc.enmus.DetailStateEnums;
import com.yh.ssc.enmus.PlanStateEnums;
import com.yh.ssc.event.NewDetailEvent;
import com.yh.ssc.event.DetailFailedEvent;
import com.yh.ssc.event.DetailSuccessEvent;
import com.yh.ssc.service.SscService;
import com.yh.ssc.service.orm.DetailOrmService;
import com.yh.ssc.service.orm.PlanOrmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-04-01 19:50
 **/

@Component
@Slf4j
public class NewDetailListener {
    
    @Resource
    private SscService sscRealService;
    @Resource
    private PlanOrmService planOrmService;
    @Resource
    private DetailOrmService detailOrmService;
    
    @EventListener(value = NewDetailEvent.class)
    public void execute(NewDetailEvent newDetailEvent){
        try {
            log.info("new plan detail:{}",newDetailEvent);
            DetailDTO detailDTO = (DetailDTO) newDetailEvent.getSource();
            List<Plan> plans = planOrmService.listByCondition(PlanQuery.builder().id(detailDTO.getPlanId()).build());
            Plan plan = plans.get(0);
            String candidate = plan.getCandidate();
            List<List<Integer>> candidateInner = JSON.parseObject(candidate, new TypeReference<List<List<Integer>>>() {});
            String sendRst = sscRealService.send(detailDTO.getGameId(),detailDTO.getCycleId(),detailDTO.getMultify(),candidateInner);
            JSONObject sendJSON = JSONObject.parseObject(sendRst);
            if (sendJSON.containsKey("errors")){
                log.info("send failed:{}",sendRst);
            }else {
                log.info("send success:{}",sendRst);
            }
            
            detailDTO.setState(DetailStateEnums.SEND_OK.getState());
            Detail detail = DetailConverter.toDO(detailDTO);
            detailOrmService.update(detail);
            List<Detail> details = detailOrmService.listByCondition(DetailQuery.builder().planId(plan.getId()).build());
            plan.setCurrent(details.size());
            planOrmService.update(plan);
        }catch (Exception e){
            log.error("NewDetailListener failed {}",e);
        }
        
    }
    
    @EventListener(value = DetailSuccessEvent.class)
    public void planSuccess(DetailSuccessEvent detailSuccessEvent) {
        try {
            log.info("plan success:{}", detailSuccessEvent);
            PlanDTO planDTO = (PlanDTO) detailSuccessEvent.getSource();
            DetailDTO detailDTO = detailSuccessEvent.getDetail();
            planDTO.setState(PlanStateEnums.SUCCESS.getState());
            Plan plan = PlanConverter.toDO(planDTO);
            planOrmService.update(plan);
            
            detailDTO.setState(DetailStateEnums.SUCCESS.getState());
            Detail detail = DetailConverter.toDO(detailDTO);
            detailOrmService.update(detail);
        }catch (Exception e) {
            log.error("plan success handler failed:{}",e);
        }
        
    }
    
    @EventListener(value = DetailFailedEvent.class)
    public void planFailed(DetailFailedEvent failedEvent) {
        try {
            log.info("plan failed:{}",failedEvent);
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
            if (round <= current) {
                planDTO.setState(PlanStateEnums.FAILED.getState());
                Plan plan = PlanConverter.toDO(planDTO);
                planOrmService.update(plan);
            } else {
                Detail newDetail = buildNewDetail(detailDTO, planDTO, currentCycleId, currentCycleValue);
                log.info("new detail:{}",newDetail);
                detailOrmService.add(newDetail);
            }
        }catch (Exception e) {
            log.error("Plan detailed planFailed handler failed,{}",e);
        }
        
    }
    
    
    private Detail buildNewDetail(DetailDTO oldDetail, PlanDTO planDTO, Long currentCycleId, String currentCycleValue) {
        Integer round = oldDetail.getRound();
        if (round >= planDTO.getRound()) {
            throw new IllegalArgumentException("round expend");
        }
        round = round+1;
        Detail newDetail = new Detail();
        newDetail.setState(DetailStateEnums.RUNNING.getState());
        newDetail.setPlanId(planDTO.getId());
        newDetail.setGameId(oldDetail.getGameId());
        
        Integer multify = planDTO.getPolicyJSON().getInteger(String.valueOf(round));
        Integer amount = multify * Common.SINGLE_AMOUNT;
        
        newDetail.setMultify(multify);
        newDetail.setRound(round);
        newDetail.setAmount(amount);
        newDetail.setCycleId(currentCycleId);
        newDetail.setCycleValue(currentCycleValue);
        newDetail.setCreateTime(new Date());
        return newDetail;
    }
    
}
