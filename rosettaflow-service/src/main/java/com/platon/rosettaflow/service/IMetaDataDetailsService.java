package com.platon.rosettaflow.service;

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
     * 根据元数据id获取元数据
     *
     * @param metaDataId 元数据id
     * @return 元数据列详情列表
     */
    List<MetaDataDetailsDto> findByMetaDataId(String metaDataId);
}
