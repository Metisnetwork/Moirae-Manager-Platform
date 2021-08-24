package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.mapper.domain.MetaDataDetails;

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
}
