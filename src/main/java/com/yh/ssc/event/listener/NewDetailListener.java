package com.yh.ssc.event.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yh.ssc.converter.DetailConverter;
import com.yh.ssc.data.dataobject.Detail;
import com.yh.ssc.data.dataobject.Plan;
import com.yh.ssc.data.query.PlanQuery;
import com.yh.ssc.dto.DetailDTO;
import com.yh.ssc.enmus.DetailStateEnums;
import com.yh.ssc.event.NewCycleEvent;
import com.yh.ssc.event.publish.NewDetailEvent;
import com.yh.ssc.service.SscDataService;
import com.yh.ssc.service.orm.DetailOrmService;
import com.yh.ssc.service.orm.PlanOrmService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-04-01 19:50
 **/

@Component
public class NewDetailListener {
    
    @Resource
    private SscDataService sscDataSimulateService;
    @Resource
    private PlanOrmService planOrmService;
    @Resource
    private DetailOrmService detailOrmService;
    
    @EventListener(value = NewDetailEvent.class)
    public void execute(NewDetailEvent newDetailEvent){
        DetailDTO detailDTO = (DetailDTO) newDetailEvent.getSource();
        List<Plan> plans = planOrmService.listByCondition(PlanQuery.builder().id(detailDTO.getPlanId()).build());
        Plan plan = plans.get(0);
        String candidate = plan.getCandidate();
        List<List<Integer>> candidateInner = JSON.parseObject(candidate, new TypeReference<List<List<Integer>>>() {});
        sscDataSimulateService.send(detailDTO.getCycleId(),detailDTO.getMultify(),candidateInner);
        
        detailDTO.setState(DetailStateEnums.SEND_OK.getState());
        Detail detail = DetailConverter.toDO(detailDTO);
        detailOrmService.update(detail);
    }
}
