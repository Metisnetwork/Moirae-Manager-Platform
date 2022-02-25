package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.dto.MetaDataDtoOld;
import com.moirae.rosettaflow.mapper.domain.MetaDataOld;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/24
 * @description 元数据服务
 */
public interface IMetaDataOldService extends IService<MetaDataOld> {

    /**
     * 获取元数据摘要列表
     *
     * @param current  当前页
     * @param size     每页大小
     * @param dataName 元数据名称
     * @return 分页数据
     */
    IPage<MetaDataDtoOld> list(Long current, Long size, String dataName);

    /**
     * 获取元数据详情
     *
     * @param metaDataPkId   元数据表Id
     * @param userMetaDataId 用户授权数据表Id
     * @return 元数据详情
     */
    MetaDataDtoOld detail(Long metaDataPkId, Long userMetaDataId);

    /**
     * 获取元数据通过ID
     *
     * @param id 元数据表Id
     * @return MetaDataOld
     */
    MetaDataOld getMetaDataById(Long id);

    /**
     * 根据identityId查询元数据列表
     *
     * @param identityId identityId
     * @return 元数据列表
     */
    List<MetaDataDtoOld> getAllAuthTables(String identityId);


    /**
     * 授权状态处理
     *
     * @param authStatus        授权状态
     * @param authMetadataState 数据状态
     * @return authStatus
     */
    Byte dealAuthStatus(Byte authStatus, Byte authMetadataState);
}
