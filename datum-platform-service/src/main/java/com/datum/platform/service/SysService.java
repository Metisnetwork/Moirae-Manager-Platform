package com.datum.platform.service;

import com.datum.platform.mapper.domain.Publicity;
import com.datum.platform.service.dto.sys.PlatONPropertiesDto;

import java.util.List;

public interface SysService {
    PlatONPropertiesDto getPlatONProperties();

    List<Publicity> listNeedSyncPublicity();

    void batchUpdatePublicity(List<Publicity> updateList);
}
