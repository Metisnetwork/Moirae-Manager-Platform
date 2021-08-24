package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.mapper.domain.MetaData;

/**
 * @author hudenian
 * @date 2021/8/24
 * @description 元数据服务
 */
public interface IMetaDataService extends IService<MetaData> {
    /**
     * 清空元数据摘要表
     */
    void truncate();
}
