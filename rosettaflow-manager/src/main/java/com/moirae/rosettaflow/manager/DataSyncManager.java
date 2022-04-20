package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.DataSync;

public interface DataSyncManager extends IService<DataSync> {
    DataSync getOneByType(String type);
}
