package com.yh.ssc.event;

import com.yh.ssc.data.dataobject.Plan;
import org.springframework.context.ApplicationEvent;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-04-01 15:40
 **/
public class PlanContinue extends ApplicationEvent {
    public PlanContinue(Plan source) {
        super(source);
    }
}
