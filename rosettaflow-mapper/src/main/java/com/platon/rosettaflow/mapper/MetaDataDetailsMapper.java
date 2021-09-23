package com.platon.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platon.rosettaflow.mapper.domain.MetaData;
import com.platon.rosettaflow.mapper.domain.MetaDataDetails;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author admin
 */
public interface MetaDataDetailsMapper extends BaseMapper<MetaDataDetails> {
    void truncate();

    /**
     * 批量插入元数据详情信息
     * @param metaDataDetailsList 元数据详情列表
     * @return 插入记录数
     */
    int batchInsert(@Param("metaDataDetailsList") List<MetaDataDetails> metaDataDetailsList);
}