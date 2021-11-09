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
     * 查询工作流节点输入授权数据
     * @param address 用户地址
     * @return
     */
    List<UserMetaDataDto> getUserMetaDataByAddress(@Param("address") String address);

    /**
     * 更新按次数授权数据的次数
     * @param metaDataIdList 元数据id集合
     * @param address 用户地址
     * @return UserMetaData
     */
    int updateTimesByMetaDataId(@Param("metaDataIdList")List<Long> metaDataIdList, @Param("address")String address);

    /**
     * 批量插入
     * @param userMetaDataList
     * @return
     */
    int batchInsert(@Param("userMetaDataList") List<UserMetaData> userMetaDataList);
}