package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.MetaDataColumnManager;
import com.datum.platform.mapper.MetaDataColumnMapper;
import com.datum.platform.mapper.domain.MetaDataColumn;
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

    @Override
    public MetaDataColumn getById(String metaDataId, int columnIndex) {
        LambdaQueryWrapper<MetaDataColumn> wrappers = Wrappers.lambdaQuery();
        wrappers.eq(MetaDataColumn::getMetaDataId, metaDataId);
        wrappers.eq(MetaDataColumn::getColumnIdx, columnIndex);
        return getOne(wrappers);
    }

    @Override
    public List<MetaDataColumn> listByIdAndIndex(String metaDataId, List<Integer> selectedColumnsV2) {
        LambdaQueryWrapper<MetaDataColumn> wrappers = Wrappers.lambdaQuery();
        wrappers.eq(MetaDataColumn::getMetaDataId, metaDataId);
        wrappers.in(MetaDataColumn::getColumnIdx, selectedColumnsV2);
        return list(wrappers);
    }
}
