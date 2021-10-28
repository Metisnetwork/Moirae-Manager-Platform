package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moirae.rosettaflow.dto.UserMetaDataDto;
import com.moirae.rosettaflow.mapper.domain.MetaDataDetails;
import com.moirae.rosettaflow.mapper.domain.UserMetaData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author admin
 */
public interface UserMetaDataMapper extends BaseMapper<UserMetaData> {

    void truncate();

    IPage<UserMetaDataDto> listByOwner(@Param("page") IPage<UserMetaData> page, @Param("address") String address, @Param("dataName") String dataName);

    List<UserMetaDataDto> getUserMetaDataByAddress(@Param("address") String address);


    int batchInsert(@Param("userMetaDataList") List<UserMetaData> userMetaDataList);
}