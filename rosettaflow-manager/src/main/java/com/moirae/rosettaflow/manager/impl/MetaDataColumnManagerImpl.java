package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.MetaDataColumnManager;
import com.moirae.rosettaflow.mapper.MetaDataColumnMapper;
import com.moirae.rosettaflow.mapper.domain.MetaDataColumn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MetaDataColumnManagerImpl extends ServiceImpl<MetaDataColumnMapper, MetaDataColumn> implements MetaDataColumnManager {

    @Override
    public List<MetaDataColumn> getList(String metaDataId) {
        LambdaQueryWrapper<MetaDataColumn> wrappers = Wrappers.lambdaQuery();
        wrappers.eq(MetaDataColumn::getMetaDataId, metaDataId);
        return list(wrappers);
    }
}
