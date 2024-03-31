package com.yh.ssc.service.orm;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yh.ssc.data.dataobject.Plan;
import com.yh.ssc.data.mapper.PlanMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-03-31 15:47
 **/
@Repository
public class PlanOrmServiceImpl extends ServiceImpl<PlanMapper, Plan> implements PlanOrmService{
    
    @Resource
    private PlanMapper planMapper;
    @Override
    public Plan add(Plan plan) {
        plan.setCreateTime(new Date());
        planMapper.insert(plan);
        return plan;
    }
    
    @Override
    public void update(Plan plan) {
        planMapper.updateById(plan);
    }
    
    
}
