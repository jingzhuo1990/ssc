package com.yh.ssc.event;

import com.yh.ssc.data.dataobject.Plan;
import com.yh.ssc.dto.PlanDTO;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-03-31 15:22
 **/
public class NewPlanEvent extends ApplicationEvent {
    
    /**
     * Create a new {@code ApplicationEvent}.
     * @param source the object on which the event initially occurred or with which the event is associated (never
     *               {@code null})
     */
    public NewPlanEvent(PlanDTO source) {
        super(source);
    }
}
