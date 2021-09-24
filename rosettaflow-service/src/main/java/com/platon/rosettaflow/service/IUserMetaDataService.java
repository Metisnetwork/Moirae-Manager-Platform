package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.dto.MetaDataDto;
import com.platon.rosettaflow.dto.UserMetaDataDto;
import com.platon.rosettaflow.mapper.domain.UserMetaData;

import java.util.List;

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
     * @param current  当前页
     * @param size     每页大小
     * @param dataName 元数据名称
     * @return 分页数据
     */
    IPage<UserMetaDataDto> list(Long current, Long size, String dataName);

    /**
     * 用户元数据授权申请
     *
     * @param userMetaDataDto 元数据授权申请对象
     */
    void auth(UserMetaDataDto userMetaDataDto);

    /**
     * 获取用户已授权机构列表
     *
     * @return 授权机构列表
     */
    List<UserMetaDataDto> getAllAuthOrganization();

    /**
     * 获取用户授权元数据列信息
     *
     * @param identityId 机构identityId
     * @return 已授权表
     */
    List<MetaDataDto> getAllAuthTables(String identityId);

    /**
     * 根据metaDataId获取用户元数据
     *
     * @param metaDataId metaDataId
     * @return 用户授权数据列表
     */
    UserMetaData getCurrentUserMetaDataByMetaDataId(String metaDataId);
}
