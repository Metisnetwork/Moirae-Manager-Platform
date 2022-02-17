package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moirae.rosettaflow.mapper.domain.MetaDataDetailsOld;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author admin
 */
public interface MetaDataDetailsOldMapper extends BaseMapper<MetaDataDetailsOld> {
    void truncate();

    /**
     * 批量插入元数据详情信息
     * @param metaDataDetailsList 元数据详情列表
     * @return 插入记录数
     */
    int batchInsert(@Param("metaDataDetailsList") List<MetaDataDetailsOld> metaDataDetailsList);

    /**
     * 批量更新元数据详情信息
     * @param metaDataDetailsList 元数据详情列表
     * @return 更新记录数
     */
    int batchUpdate(@Param("metaDataDetailsList") List<MetaDataDetailsOld> metaDataDetailsList);

    /**
     * 查询已存在的数据
     * @param newMetaDataDetailsList
     * @return
     */
    List<MetaDataDetailsOld> existMetaDataIdAndColumnList(@Param("metaDataDetailsList") List<MetaDataDetailsOld> newMetaDataDetailsList);
}
