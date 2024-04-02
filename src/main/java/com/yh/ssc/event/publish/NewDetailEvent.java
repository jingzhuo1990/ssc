package com.yh.ssc.event.publish;

import com.yh.ssc.dto.DetailDTO;
import com.yh.ssc.dto.PlanDTO;
import org.springframework.context.ApplicationEvent;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-03-31 15:22
 **/
public class NewDetailEvent extends ApplicationEvent {
    
    /**
     * Create a new {@code ApplicationEvent}.
     * @param source the object on which the event initially occurred or with which the event is associated (never
     *               {@code null})
     */
    public NewDetailEvent(DetailDTO source) {
        super(source);
    }
}
