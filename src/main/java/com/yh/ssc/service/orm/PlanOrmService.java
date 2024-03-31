package com.yh.ssc.service.orm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yh.ssc.data.dataobject.Plan;
import com.yh.ssc.data.dataobject.SscData;
import com.yh.ssc.data.query.PlanQuery;
import com.yh.ssc.data.query.SscDataQuery;
import com.yh.ssc.dto.SscDataDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public interface PlanOrmService extends IService<Plan> {
    
    
    Plan add(Plan plan);
    
    void update(Plan plan);
    
    default List<Plan> listByCondition(PlanQuery query) {
        QueryWrapper<Plan> queryWrapper = new QueryWrapper<>();
        String startCycleValue = query.getStartCycleValue();
        if (StringUtils.isNotEmpty(query.getStartCycleValue())){
            queryWrapper.eq("start_cycle_value",startCycleValue);
        }
        return list(queryWrapper);
    }
}
