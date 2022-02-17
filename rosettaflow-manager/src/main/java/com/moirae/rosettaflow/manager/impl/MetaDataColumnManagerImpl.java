package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.MetaDataColumnManager;
import com.moirae.rosettaflow.mapper.MetaDataColumnMapper;
import com.moirae.rosettaflow.mapper.domain.MetaDataColumn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MetaDataColumnManagerImpl extends ServiceImpl<MetaDataColumnMapper, MetaDataColumn> implements MetaDataColumnManager {

}
