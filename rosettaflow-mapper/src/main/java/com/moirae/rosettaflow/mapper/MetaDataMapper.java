package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moirae.rosettaflow.dto.MetaDataDto;
import com.moirae.rosettaflow.mapper.domain.MetaData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author admin
 */
public interface MetaDataMapper extends BaseMapper<MetaData> {
    /**
     * 清空表
     */
    void truncate();

    /**
     * 根据identityId、钱包地址，查询元数据列表
     *
     * @param identityId identityId
     * @param address address
     * @return 元数据列表
     */
    List<MetaDataDto> getAllAuthTables(@Param("identityId") String identityId, @Param("address") String address);

    /**
     * 批量插入元数据信息
     * @param metaDataList 元数据列表
     * @return 插入记录数
     */
    int batchInsert(@Param("metaDataList") List<MetaData> metaDataList);
}
