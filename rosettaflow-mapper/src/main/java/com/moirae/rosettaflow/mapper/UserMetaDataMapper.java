package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moirae.rosettaflow.dto.UserMetaDataDto;
import com.moirae.rosettaflow.mapper.domain.UserMetaData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author admin
 */
public interface UserMetaDataMapper extends BaseMapper<UserMetaData> {

    void truncate();

    /**
     * 分页查询授权激励
     * @param page
     * @param address
     * @param dataName
     * @return
     */
    IPage<UserMetaDataDto> listByOwner(@Param("page") IPage<UserMetaData> page, @Param("address") String address, @Param("dataName") String dataName);

    /**
     * 查询用户授权数据组织信息
     * @param address
     * @return
     */
    List<UserMetaDataDto> getUserMetaDataByAddress(@Param("address") String address);

    /**
     * 批量插入
     * @param userMetaDataList
     * @return
     */
    int batchInsert(@Param("userMetaDataList") List<UserMetaData> userMetaDataList);

    /**
     * 批量修改
     * @param userMetaDataList
     * @return
     */
    int batchUpdate(@Param("userMetaDataList") List<UserMetaData> userMetaDataList);

    /**
     * 查询已存在的metaDataAuthId
     * @param userMetaDataAuthIdList
     * @return
     */
    List<String> existMetaDataAuthIdList(@Param("userMetaDataAuthIdList") List<String> userMetaDataAuthIdList);
}
