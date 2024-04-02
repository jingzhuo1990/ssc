package com.yh.ssc.event.publish;

import com.yh.ssc.dto.DetailDTO;
import com.yh.ssc.dto.PlanDTO;
import com.yh.ssc.dto.SscDataDTO;
import com.yh.ssc.event.NewCycleEvent;
import com.yh.ssc.event.NewPlanEvent;
import com.yh.ssc.event.PlanDetailFailedEvent;
import com.yh.ssc.event.PlanSuccessEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-03-31 14:55
 **/
@Component
public class EventPublisher implements ApplicationEventPublisherAware {
    
    private ApplicationEventPublisher applicationEventPublisher;
    
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
    
    public void publishCycleEvent(SscDataDTO sscData) {
        NewCycleEvent newCycleEvent = new NewCycleEvent(sscData);
        applicationEventPublisher.publishEvent(newCycleEvent);
    }
    
    public void publishNewPlanEvent(PlanDTO plan){
        NewPlanEvent newPlanEvent = new NewPlanEvent(plan);
        applicationEventPublisher.publishEvent(newPlanEvent);
    }
    
    public void publishPlanSuccess(PlanDTO plan, DetailDTO detail){
        PlanSuccessEvent successEvent = new PlanSuccessEvent(plan,detail);
        applicationEventPublisher.publishEvent(successEvent);
    }
    
    public void publishDetailFail(SscDataDTO sscDataDTO,PlanDTO plan,DetailDTO detail){
        PlanDetailFailedEvent failedEvent = new PlanDetailFailedEvent(sscDataDTO,plan,detail);
        applicationEventPublisher.publishEvent(failedEvent);
    }
    
    public void publishNewDetail(DetailDTO detailDTO){
        NewDetailEvent detailEvent = new NewDetailEvent(detailDTO);
        applicationEventPublisher.publishEvent(detailEvent);
    }
}
