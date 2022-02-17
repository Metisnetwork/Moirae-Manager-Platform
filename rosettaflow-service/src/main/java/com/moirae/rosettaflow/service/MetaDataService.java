package com.moirae.rosettaflow.service;

import com.moirae.rosettaflow.mapper.domain.MetaData;
import com.moirae.rosettaflow.mapper.domain.MetaDataColumn;

import java.util.List;

public interface MetaDataService {
    
    void batchReplace(List<MetaData> metaDataList, List<MetaDataColumn> metaDataColumnList);
}
