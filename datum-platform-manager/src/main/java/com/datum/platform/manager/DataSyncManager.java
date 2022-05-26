package com.datum.platform.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datum.platform.mapper.domain.DataSync;

public interface DataSyncManager extends IService<DataSync> {
    DataSync getOneByType(String type);
}
