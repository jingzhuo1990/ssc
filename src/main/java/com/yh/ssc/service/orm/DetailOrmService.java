package com.yh.ssc.service.orm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yh.ssc.data.dataobject.Detail;
import com.yh.ssc.data.dataobject.Plan;
import com.yh.ssc.data.query.DetailQuery;
import com.yh.ssc.data.query.PlanQuery;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public interface DetailOrmService extends IService<Detail> {
    
    Detail add(Detail detail);
    
    void update(Detail detail);
    
    default List<Detail> listByCondition(DetailQuery query) {
        QueryWrapper<Detail> queryWrapper = new QueryWrapper<>();
        Long planId = query.getPlanId();
        Long cycleId = query.getCycleId();
        String cycleValue = query.getCycleValue();
        Long gameId = query.getGameId();
        
        if (planId!=null && planId>0){
            queryWrapper.eq("plan_id",planId);
        }
        if (cycleId!=null && cycleId>0){
            queryWrapper.eq("cycle_id",cycleId);
        }
        if (StringUtils.isNotEmpty(cycleValue)){
            queryWrapper.eq("cycle_value",cycleValue);
        }
        if (gameId!=null && gameId>0){
            queryWrapper.eq("game_id",gameId);
        }
        
        return list(queryWrapper);
    }
}
