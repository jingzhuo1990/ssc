package com.yh.ssc.event;

import com.alibaba.fastjson.JSONObject;
import com.yh.ssc.dto.DetailDTO;
import com.yh.ssc.dto.PlanDTO;
import com.yh.ssc.dto.SscDataDTO;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-03-31 15:22
 **/
@ToString
public class DetailFailedEvent extends ApplicationEvent {
    
    private DetailDTO detail;
    
    private SscDataDTO sscDataDTO;
    
    /**
     * Create a new {@code ApplicationEvent}.
     * @param source the object on which the event initially occurred or with which the event is associated (never
     *               {@code null})
     */
    public DetailFailedEvent(SscDataDTO sscDataDTO,PlanDTO source,DetailDTO detail) {
        super(source);
        this.detail = detail;
        this.sscDataDTO = sscDataDTO;
    }
    
    public DetailDTO getDetail() {
        return detail;
    }
    
    public SscDataDTO getSscDataDTO() {
        return sscDataDTO;
    }
    
    @Override
    public String toString() {
        return getClass().getName() + "[source=" + source + "]"+"sscData:{}"+ JSONObject.toJSONString(sscDataDTO)+";detail:"+JSONObject.toJSONString(detail);
    }
}
