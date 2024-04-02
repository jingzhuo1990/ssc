package com.yh.ssc.service.orm;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yh.ssc.converter.DetailConverter;
import com.yh.ssc.data.dataobject.Detail;
import com.yh.ssc.data.mapper.DetailMapper;
import com.yh.ssc.dto.DetailDTO;
import com.yh.ssc.event.publish.EventPublisher;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-03-31 15:48
 **/

@Repository
public class DetailOrmServiceImpl extends ServiceImpl<DetailMapper, Detail> implements DetailOrmService{
    
    @Resource
    private DetailMapper detailMapper;
    @Resource
    private EventPublisher eventPublisher;
    
    @Override
    public Detail add(Detail detail) {
        detail.setCreateTime(new Date());
        detailMapper.insert(detail);
        DetailDTO detailDTO = DetailConverter.toDTO(detail);
        eventPublisher.publishNewDetail(detailDTO);
        return detail;
    }
    
    @Override
    public void update(Detail detail) {
        detailMapper.updateById(detail);
    }
}
