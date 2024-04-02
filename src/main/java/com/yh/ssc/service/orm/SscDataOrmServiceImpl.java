package com.yh.ssc.service.orm;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yh.ssc.converter.SscDataConverter;
import com.yh.ssc.data.dataobject.SscData;
import com.yh.ssc.data.mapper.SscDataMapper;
import com.yh.ssc.dto.SscDataDTO;
import com.yh.ssc.event.publish.EventPublisher;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @program: mediAsst
 * @description:
 * @author: yehang
 * @create: 2024-03-29 11:22
 **/
@Repository
public class SscDataOrmServiceImpl extends ServiceImpl<SscDataMapper, SscData> implements SscDataOrmService {
    
    @Resource
    private SscDataMapper sscDataMapper;
    
    @Resource
    private EventPublisher eventPublisher;
    
    @Override
    public SscData add(SscData sscData) {
        try {
            sscData.setCreateTime(new Date());
            sscDataMapper.insert(sscData);
            SscDataDTO sscDataDTO = SscDataConverter.toDTO(sscData);
            eventPublisher.publishCycleEvent(sscDataDTO);
            return sscData;
        }catch (Exception e){
            log.error("error inserting sscdata,e:{}",e);
            throw new RuntimeException("error inserting sscdata");
        }
    }
}
