package com.yh.ssc.converter;

import com.yh.ssc.data.dataobject.Detail;
import com.yh.ssc.dto.DetailDTO;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-04-01 19:02
 **/
public class DetailConverter {

    public static DetailDTO toDTO(Detail detail) {
        DetailDTO detailDTO = new DetailDTO();
        detailDTO.setId(detail.getId());
        detailDTO.setAmount(detail.getAmount());
        detailDTO.setCreateTime(detail.getCreateTime());
        detailDTO.setCycleId(detail.getCycleId());
        detailDTO.setProfit(detail.getProfit());
        detailDTO.setRound(detail.getRound());
        detailDTO.setState(detail.getState());
        detailDTO.setCycleValue(detail.getCycleValue());
        detailDTO.setPlanId(detail.getPlanId());
        return detailDTO;
    }
    
    public static Detail toDO(DetailDTO detailDTO) {
        Detail detail = new Detail();
        detail.setId(detailDTO.getId());
        detail.setAmount(detailDTO.getAmount());
        detail.setCreateTime(detailDTO.getCreateTime());
        detail.setCycleId(detailDTO.getCycleId());
        detail.setProfit(detailDTO.getProfit());
        detail.setRound(detailDTO.getRound());
        detail.setState(detailDTO.getState());
        detail.setCycleValue(detailDTO.getCycleValue());
        detail.setPlanId(detailDTO.getPlanId());
        return detail;
    }
}
