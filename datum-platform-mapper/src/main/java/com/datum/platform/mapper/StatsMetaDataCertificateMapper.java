package com.datum.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.datum.platform.mapper.domain.StatsMetaDataCertificate;

import java.util.List;

/**
 * @author admin
 */
public interface StatsMetaDataCertificateMapper extends BaseMapper<StatsMetaDataCertificate> {
    List<StatsMetaDataCertificate> getDataTokenUsedTop(Integer size);
}
