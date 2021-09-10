package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.dto.MetaDataDetailsDto;
import com.platon.rosettaflow.mapper.domain.MetaDataDetails;

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
     * @param current 元数据详情中列数据当前页
     * @param size    元数据详情中列数据每页大小
     * @return 元数据列详情列表
     */
    IPage<MetaDataDetailsDto> findByMetaDataId(String metaDataId, Long current, Long size);

    /**
     * 根据id获取元数据详情
     *
     * @param id 元数据详情表id
     * @param current 元数据详情中列数据当前页
     * @param size    元数据详情中列数据每页大小
     * @return 元数据列详情列表
     */
    IPage<MetaDataDetailsDto> findById(Long id, Long current, Long size);
}
