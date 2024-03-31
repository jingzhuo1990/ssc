package com.yh.ssc.service.plan;

import com.google.common.collect.Lists;
import com.yh.ssc.dto.DataContext;
import com.yh.ssc.data.dataobject.SscData;
import com.yh.ssc.enmus.IndexEnums;
import com.yh.ssc.event.NewCycleEvent;
import com.yh.ssc.service.DataFactoryService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-03-31 13:00
 **/
@Service
public class SinglePlanServiceImpl implements SinglePlanService {
    
    @Resource
    private DataFactoryService dataFactoryService;
    
    @EventListener(value = NewCycleEvent.class)
    @Override
    public void plan(NewCycleEvent newCycleEvent) {
        
        
        
        SscData sscData = (SscData) newCycleEvent.getSource();
        DataContext dataContext = dataFactoryService.buildDataContext(25);
        List<DataContext.SinglePercent> singlePercents = dataContext.getSinglePercents();
        
        List<List<String>> canditate = Lists.newArrayList();
        for (DataContext.SinglePercent singlePercent:singlePercents){
            IndexEnums indexEnums = singlePercent.getIndexEnums();
            List<String> candidateNum = singlePercent.getDistributeds().stream().filter(x->x.getCnt()==0).map(
                    DataContext.Distributed::getNum).map(z-> String.valueOf(z)).collect(Collectors.toList());
            
            canditate.add(indexEnums.getIndex(),candidateNum);
        }
        
    }
}
