package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.dto.MetaDataDetailsDto;
import com.moirae.rosettaflow.mapper.domain.MetaDataDetails;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/24
 * @description 元数据请情服务
 */
public interface IMetaDataDetailsService extends IService<MetaDataDetails> {
    /**
     * 清空元数据详情
     */
    void truncate();

    /**
     * 根据元数据metaDataId获取元数据 columnList
     *
     * @param metaDataId 元数据id
     * @param current    元数据详情中列数据当前页
     * @param size       元数据详情中列数据每页大小
     * @return 元数据列详情列表
     */
    IPage<MetaDataDetailsDto> findByMetaDataId(String metaDataId, Long current, Long size);

    /**
     * 根据id获取元数据详情
     *
     * @param id      元数据详情表id
     * @param current 元数据详情中列数据当前页
     * @param size    元数据详情中列数据每页大小
     * @return 元数据列详情列表
     */
    IPage<MetaDataDetailsDto> findById(Long id, Long current, Long size);

    /**
     * 获取用户授权元数据列详情列表
     *
     * @param metaDataId 元数据id
     * @return 授权元数据列表
     */
    List<MetaDataDetailsDto> getAllAuthColumns(String metaDataId);

    /**
     * 批量插入列表
     *
     * @param metaDataDetailsList 插入列表集合
     * @return 插入记录数
     */
    int batchInsert(List<MetaDataDetails> metaDataDetailsList);

    /**
     * 根据元数据列id获取元数据列详情
     *
     * @param id 元数据列id
     * @return 元数据列详情
     */
    MetaDataDetails getColumnIndexById(Long id);

    /**
     * 根据t_meta_data_details表id数组查询所有的索引列信息
     *
     * @param columnIdsArr id数组
     * @return 索引列集合
     */
    List<Integer> getColumnIndexByIds(Object[] columnIdsArr);

    /**
     * 根据metaDataId和columnIndex更新数据
     *
     * @param metaDatadetails
     * @return
     */
    void batchUpdateByMetaDataIdAndColumnIndex(List<MetaDataDetails> metaDatadetails);

    /**
     * 查询已存在的数据
     * @param newMetaDataDetailsList
     * @return
     */
    List<MetaDataDetails> existMetaDataIdAndColumnList(List<MetaDataDetails> newMetaDataDetailsList);
}
