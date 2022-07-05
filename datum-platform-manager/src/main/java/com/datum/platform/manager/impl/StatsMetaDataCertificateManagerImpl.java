package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.StatsMetaDataCertificateManager;
import com.datum.platform.mapper.StatsMetaDataCertificateMapper;
import com.datum.platform.mapper.domain.StatsMetaDataCertificate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class StatsMetaDataCertificateManagerImpl extends ServiceImpl<StatsMetaDataCertificateMapper, StatsMetaDataCertificate> implements StatsMetaDataCertificateManager {

    @Override
    public List<StatsMetaDataCertificate> getDataTokenUsedTop(Integer size) {
        return this.baseMapper.getDataTokenUsedTop(size);
    }
}
