package com.moirae.rosettaflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.common.enums.StatusEnum;
import com.moirae.rosettaflow.dto.MetaDataDetailsDto;
import com.moirae.rosettaflow.mapper.MetaDataDetailsOldMapper;
import com.moirae.rosettaflow.mapper.domain.MetaDataDetailsOld;
import com.moirae.rosettaflow.service.IMetaDataDetailsOldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/24
 * @description 元数据服务实现类
 */
@Slf4j
@Service
public class MetaDataDetailsOldServiceImpl extends ServiceImpl<MetaDataDetailsOldMapper, MetaDataDetailsOld> implements IMetaDataDetailsOldService {
    @Override
    public void truncate() {
        this.baseMapper.truncate();
    }

    @Override
    public IPage<MetaDataDetailsDto> findByMetaDataId(String metaDataId, Long current, Long size) {
        Page<MetaDataDetailsOld> page = new Page<>(current, size);
        LambdaQueryWrapper<MetaDataDetailsOld> wrapper = getQueryWrapper(metaDataId, null);
        this.page(page, wrapper);
        return this.convertToPageDto(page);
    }

    @Override
    public List<MetaDataDetailsDto> getAllAuthColumns(String metaDataId) {
        LambdaQueryWrapper<MetaDataDetailsOld> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MetaDataDetailsOld::getMetaDataId, metaDataId);
        wrapper.eq(MetaDataDetailsOld::getStatus, StatusEnum.VALID.getValue());
        wrapper.orderByAsc(MetaDataDetailsOld::getColumnIndex);
        List<MetaDataDetailsOld> metaDataDetailsList = this.list(wrapper);
        return BeanUtil.copyToList(metaDataDetailsList, MetaDataDetailsDto.class);
    }

    @Override
    public int batchInsert(List<MetaDataDetailsOld> metaDataDetailsList) {
        return this.baseMapper.batchInsert(metaDataDetailsList);
    }

    @Override
    public MetaDataDetailsOld getColumnIndexById(Long id) {
        LambdaQueryWrapper<MetaDataDetailsOld> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MetaDataDetailsOld::getId, id);
        wrapper.eq(MetaDataDetailsOld::getStatus, StatusEnum.VALID.getValue());
        MetaDataDetailsOld mtaDataDetails = this.getOne(wrapper);
        return mtaDataDetails == null ? new MetaDataDetailsOld() : mtaDataDetails;
    }

    @Override
    public List<Integer> getColumnIndexByIds(Object[] columnIdsArr) {
        List<Integer> columnIndexList = new ArrayList<>();
        LambdaQueryWrapper<MetaDataDetailsOld> wrapper = Wrappers.lambdaQuery();
        wrapper.in(MetaDataDetailsOld::getId, columnIdsArr);
        this.list(wrapper).forEach(m -> columnIndexList.add(m.getColumnIndex()));
        return columnIndexList;
    }

    @Override
    public void batchUpdateByMetaDataIdAndColumnIndex(List<MetaDataDetailsOld> metaDatadetails) {
        this.baseMapper.batchUpdate(metaDatadetails);
    }

    @Override
    public List<MetaDataDetailsOld> existMetaDataIdAndColumnList(List<MetaDataDetailsOld> newMetaDataDetailsList) {
        return this.baseMapper.existMetaDataIdAndColumnList(newMetaDataDetailsList);
    }

    private LambdaQueryWrapper<MetaDataDetailsOld> getQueryWrapper(String metaDataId, Long id) {
        LambdaQueryWrapper<MetaDataDetailsOld> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MetaDataDetailsOld::getStatus, StatusEnum.VALID.getValue());
        if (StrUtil.isNotBlank(metaDataId)) {
            wrapper.eq(MetaDataDetailsOld::getMetaDataId, metaDataId);
        }
        if (id != null) {
            wrapper.eq(MetaDataDetailsOld::getId, id);
        }
        return wrapper;
    }

    IPage<MetaDataDetailsDto> convertToPageDto(Page<?> page) {
        IPage<MetaDataDetailsDto> pageDto = new Page<>();
        pageDto.setCurrent(page.getCurrent());
        pageDto.setRecords(BeanUtil.copyToList(page.getRecords(), MetaDataDetailsDto.class));
        pageDto.setSize(page.getSize());
        pageDto.setTotal(page.getTotal());
        return pageDto;
    }
}
