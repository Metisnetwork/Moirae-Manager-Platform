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
     * 根据钱包地址查询元数据
     *
     * @param address 钱包地址
     * @return 钱包地址查询到授权数据
     */
    List<MetaDataDto> selectMetaDataWithAuth(String address);

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

    /**
     * 批量更新元数据信息
     * @param metaDataList 元数据列表
     * @return 插入记录数
     */
    int batchUpdate(@Param("metaDataList") List<MetaData> metaDataList);

    /**
     * 查询出已存在的metaDataIdList
     * @param metaDataIdList
     * @return
     */
    List<String> existMetaDataIdList(@Param("metaDataIdList") List<String> metaDataIdList);
}