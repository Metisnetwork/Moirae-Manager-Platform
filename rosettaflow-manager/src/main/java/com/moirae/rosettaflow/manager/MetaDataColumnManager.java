package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.MetaDataColumn;

import java.util.List;

public interface MetaDataColumnManager extends IService<MetaDataColumn> {

    List<MetaDataColumn> getList(String metaDataId);

    MetaDataColumn getById(String metaDataId, int columnIndex);
}
