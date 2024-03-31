package com.yh.ssc.service.plan;

import com.yh.ssc.event.NewCycleEvent;

import java.util.List;

public interface SinglePlanService {

    void plan(NewCycleEvent newCycleEvent);
}
