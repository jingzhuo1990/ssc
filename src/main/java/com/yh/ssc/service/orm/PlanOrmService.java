package com.yh.ssc.service.orm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yh.ssc.data.dataobject.Plan;
import com.yh.ssc.data.dataobject.SscData;
import com.yh.ssc.data.query.PlanQuery;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public interface PlanOrmService extends IService<Plan> {
    
    
    Plan add(Plan plan);
    
    void update(Plan plan);
    
    default List<Plan> listByCondition(PlanQuery query) {
        QueryWrapper<Plan> queryWrapper = new QueryWrapper<>();
        String startCycleValue = query.getStartCycleValue();
        Long planId = query.getId();
        String subType = query.getSubType();
        Integer singleCan = query.getSingleCan();
        if (StringUtils.isNotEmpty(query.getStartCycleValue())){
            queryWrapper.eq("start_cycle_value",startCycleValue);
        }
        if (planId!=null && planId>0){
            queryWrapper.eq("id",planId);
        }
        if (CollectionUtils.isNotEmpty(query.getCycleValues())){
            queryWrapper.in("start_cycle_value",query.getCycleValues());
        }
        if (StringUtils.isNotEmpty(query.getType())){
            queryWrapper.eq("type",query.getType());
        }
        if (StringUtils.isNotEmpty(subType)){
            queryWrapper.eq("sub_type",subType);
        }
        if (singleCan!=null){
            queryWrapper.eq("single_can",singleCan);
        }
        queryWrapper.orderByDesc("id");
        Page<Plan> page = new Page<>(1, 20);
        Page<Plan> planPage = page(page,queryWrapper);
        List<Plan> plan = planPage.getRecords();
        
        return plan;
    }
}
