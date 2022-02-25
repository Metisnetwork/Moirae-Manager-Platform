package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moirae.rosettaflow.dto.MetaDataDtoOld;
import com.moirae.rosettaflow.mapper.domain.MetaDataOld;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author admin
 */
public interface MetaDataOldMapper extends BaseMapper<MetaDataOld> {

    /**
     * 根据identityId、钱包地址，查询元数据列表
     *
     * @param identityId identityId
     * @param address address
     * @return 元数据列表
     */
    List<MetaDataDtoOld> getAllAuthTables(@Param("identityId") String identityId, @Param("address") String address);
}
