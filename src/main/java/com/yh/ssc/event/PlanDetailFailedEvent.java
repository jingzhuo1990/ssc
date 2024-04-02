package com.yh.ssc.event;

import com.yh.ssc.data.dataobject.Detail;
import com.yh.ssc.data.dataobject.Plan;
import com.yh.ssc.dto.DetailDTO;
import com.yh.ssc.dto.PlanDTO;
import com.yh.ssc.dto.SscDataDTO;
import org.springframework.context.ApplicationEvent;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-03-31 15:22
 **/
public class PlanDetailFailedEvent extends ApplicationEvent {
    
    private DetailDTO detail;
    private Long currentCycleId;
    
    private String currentCycleValue;
    
    private SscDataDTO sscDataDTO;
    
    /**
     * Create a new {@code ApplicationEvent}.
     * @param source the object on which the event initially occurred or with which the event is associated (never
     *               {@code null})
     */
    public PlanDetailFailedEvent(SscDataDTO sscDataDTO,PlanDTO source,DetailDTO detail) {
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
}
