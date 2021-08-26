package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.dto.UserMetaDataDto;
import com.platon.rosettaflow.mapper.domain.UserMetaData;

/**
 * @author hudenian
 * @date 2021/8/24
 * @description 用户元数据授权服务
 */
public interface IUserMetaDataService extends IService<UserMetaData> {
    /**
     * 清空用户授权数据信息
     */
    void truncate();

    /**
     * 获取用户授权元数据摘要列表
     *
     * @param current 当前页
     * @param size    每页大小
     * @return 分页数据
     */
    IPage<UserMetaDataDto> list(Long current, Long size);
}
