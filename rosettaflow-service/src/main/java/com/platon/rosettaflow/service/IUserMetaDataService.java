package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
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
}
