package com.platon.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platon.rosettaflow.dto.UserMetaDataDto;
import com.platon.rosettaflow.mapper.domain.UserMetaData;
import org.apache.ibatis.annotations.Param;

/**
 * @author admin
 */
public interface UserMetaDataMapper extends BaseMapper<UserMetaData> {

    /**
     *
     */
    void truncate();

    /**
     *  查询我的元数据列表，根据钱包地址及文件名称
     * @param page
     * @param address
     * @param dataName
     * @return
     */
    IPage<UserMetaDataDto> listByOwner(@Param("page") IPage<UserMetaData> page, @Param("address") String address, @Param("dataName") String dataName);
}