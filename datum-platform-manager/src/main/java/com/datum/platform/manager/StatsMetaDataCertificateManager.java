package com.datum.platform.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datum.platform.mapper.domain.StatsMetaDataCertificate;

import java.util.List;

public interface StatsMetaDataCertificateManager extends IService<StatsMetaDataCertificate> {

    List<StatsMetaDataCertificate> getDataTokenUsedTop(Integer size);
}
