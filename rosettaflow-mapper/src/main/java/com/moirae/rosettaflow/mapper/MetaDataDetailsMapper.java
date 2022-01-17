package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moirae.rosettaflow.mapper.domain.MetaData;
import com.moirae.rosettaflow.mapper.domain.MetaDataDetails;
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

    /**
     * 批量更新元数据详情信息
     * @param metaDataDetailsList 元数据详情列表
     * @return 更新记录数
     */
    int batchUpdate(@Param("metaDataDetailsList") List<MetaDataDetails> metaDataDetailsList);

    /**
     * 查询已存在的数据
     * @param newMetaDataDetailsList
     * @return
     */
    List<MetaDataDetails> existMetaDataIdAndColumnList(@Param("metaDataDetailsList") List<MetaDataDetails> newMetaDataDetailsList);
}