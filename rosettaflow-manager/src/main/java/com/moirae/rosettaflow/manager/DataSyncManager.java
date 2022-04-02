package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.DataSync;
import com.moirae.rosettaflow.mapper.enums.DataSyncTypeEnum;

public interface DataSyncManager extends IService<DataSync> {
    DataSync getOneByType(String type);
}
