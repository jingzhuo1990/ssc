package com.yh.ssc.event;

import com.alibaba.fastjson.JSONObject;
import com.yh.ssc.dto.DetailDTO;
import com.yh.ssc.dto.PlanDTO;
import org.springframework.context.ApplicationEvent;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-03-31 15:22
 **/
public class DetailSuccessEvent extends ApplicationEvent {
    
    private DetailDTO detail;
    
    /**
     * Create a new {@code ApplicationEvent}.
     * @param source the object on which the event initially occurred or with which the event is associated (never
     *               {@code null})
     */
    public DetailSuccessEvent(PlanDTO source, DetailDTO detail) {
        super(source);
        this.detail = detail;
    }
    
    public DetailDTO getDetail() {
        return detail;
    }
    
    @Override
    public String toString() {
        return getClass().getName() + "[source=" + source + "]"+";detail:"+JSONObject.toJSONString(detail);
    }
}
