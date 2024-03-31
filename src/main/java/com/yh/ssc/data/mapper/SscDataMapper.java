package com.yh.ssc.data.mapper;

/**
 * @program: mediAsst
 * @description:
 * @author: yehang
 * @create: 2024-03-29 11:25
 **/

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yh.ssc.data.dataobject.SscData;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SscDataMapper extends BaseMapper<SscData> {

}
