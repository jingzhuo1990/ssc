package com.yh.ssc.event.publish;

import com.yh.ssc.data.dataobject.SscData;
import com.yh.ssc.event.NewCycleEvent;
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
public class NewCycleEventPublisher implements ApplicationEventPublisherAware {
    
    private ApplicationEventPublisher applicationEventPublisher;
    
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
    
    public void publishCustomEvent(SscData sscData) {
        NewCycleEvent customEvent = new NewCycleEvent(sscData);
        applicationEventPublisher.publishEvent(customEvent);
    }
}
