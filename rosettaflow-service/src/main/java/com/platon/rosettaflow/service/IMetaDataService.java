package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.dto.MetaDataDto;
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

    /**
     * 获取元数据摘要列表
     *
     * @param current  当前页
     * @param size     每页大小
     * @param dataName 元数据名称
     * @return 分页数据
     */
    IPage<MetaDataDto> list(Long current, Long size, String dataName);

    /**
     * 获取元数据详情
     *
     * @param id 元数据id
     * @return 元数据详情
     */
    MetaDataDto detail(Long id);
}
