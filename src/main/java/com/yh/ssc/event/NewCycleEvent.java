package com.yh.ssc.event;

import com.yh.ssc.data.dataobject.SscData;
import org.springframework.context.ApplicationEvent;

/**
 * @program: ssc
 * @description: 新的一轮事件
 * @author: yehang
 * @create: 2024-03-31 14:52
 **/
public class NewCycleEvent extends ApplicationEvent {
    
    /**
     * Create a new {@code ApplicationEvent}.
     * @param sscData the object on which the event initially occurred or with which the event is associated (never
     *               {@code null})
     */
    public NewCycleEvent(SscData sscData) {
        super(sscData);
    }
    
    
}
