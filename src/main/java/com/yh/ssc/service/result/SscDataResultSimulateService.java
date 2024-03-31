package com.yh.ssc.service.result;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.yh.ssc.dto.SscDataDTO;
import com.yh.ssc.data.query.SscDataQuery;
import com.yh.ssc.service.orm.SscDataOrmService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-03-31 14:39
 **/
@Service
public class SscDataResultSimulateService implements SscDataResultService{
    
    @Resource
    private SscDataOrmService sscDataOrmService;
    
    @Override
    public List<Integer> query(Long gameId, Long cycleId) {
        List<Integer> rst = Lists.newArrayList();
        List<SscDataDTO> sscDataDTOS = sscDataOrmService.listByCondition(SscDataQuery.builder().gameId(gameId).cycleId(cycleId).build());
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(sscDataDTOS),"query data result by simulated is empty");
        SscDataDTO sscDataDTO = sscDataDTOS.get(0);
        rst.add(sscDataDTO.getWan());
        rst.add(sscDataDTO.getQian());
        rst.add(sscDataDTO.getBai());
        rst.add(sscDataDTO.getShi());
        rst.add(sscDataDTO.getGe());
        return rst;
    }
}
