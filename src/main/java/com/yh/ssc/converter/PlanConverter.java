package com.yh.ssc.converter;

import com.alibaba.fastjson.JSONObject;
import com.yh.ssc.data.dataobject.Plan;
import com.yh.ssc.dto.PlanDTO;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-04-01 17:00
 **/
public class PlanConverter {
    
    public static PlanDTO toDTO(Plan plan){
        PlanDTO planDTO = new PlanDTO();
        planDTO.setId(plan.getId());
        planDTO.setCandidate(plan.getCandidate());
        planDTO.setCurrent(plan.getCurrent());
        planDTO.setPolicy(plan.getPolicy());
        planDTO.setCreateTime(plan.getCreateTime());
        planDTO.setRound(plan.getRound());
        planDTO.setType(plan.getType());
        planDTO.setSubType(plan.getSubType());
        planDTO.setState(plan.getState());
        planDTO.setCandidateInner(plan.getCandidateInner());
        planDTO.setSingleCan(plan.getSingleCan());
        planDTO.setStartCycleId(plan.getStartCycleId());
        planDTO.setStartCycleValue(plan.getStartCycleValue());
        planDTO.setGameId(plan.getGameId());
        
        JSONObject policyJSON = JSONObject.parseObject(plan.getPolicy());
        planDTO.setPolicyJSON(policyJSON);
        return planDTO;
    }
    
    public static Plan toDO(PlanDTO planDTO){
        Plan plan = new Plan();
        plan.setId(planDTO.getId());
        plan.setCandidate(planDTO.getCandidate());
        plan.setCurrent(planDTO.getCurrent());
        plan.setPolicy(planDTO.getPolicy());
        plan.setCreateTime(planDTO.getCreateTime());
        plan.setRound(planDTO.getRound());
        plan.setType(planDTO.getType());
        plan.setState(planDTO.getState());
        plan.setSubType(planDTO.getSubType());
        plan.setSingleCan(planDTO.getSingleCan());
        plan.setGameId(planDTO.getGameId());
        return plan;
    }
}
