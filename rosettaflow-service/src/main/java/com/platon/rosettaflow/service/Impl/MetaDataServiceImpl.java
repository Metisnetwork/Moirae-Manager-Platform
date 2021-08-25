package com.platon.rosettaflow.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.enums.MetaDataStatusEnum;
import com.platon.rosettaflow.common.utils.BeanCopierUtils;
import com.platon.rosettaflow.dto.MetaDataDto;
import com.platon.rosettaflow.mapper.MetaDataMapper;
import com.platon.rosettaflow.mapper.domain.MetaData;
import com.platon.rosettaflow.service.IMetaDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/24
 * @description 功能描述
 */
@Slf4j
@Service
public class MetaDataServiceImpl extends ServiceImpl<MetaDataMapper, MetaData> implements IMetaDataService {

    @Override
    public void truncate() {
        this.baseMapper.truncate();
    }

    @Override
    public IPage<MetaDataDto> list(Long current, Long size) {
        Page<MetaData> page = new Page<>(current, size);
        LambdaQueryWrapper<MetaData> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MetaData::getDataStatus, MetaDataStatusEnum.RELEASE.getValue());
        this.page(page, wrapper);
        return this.convertToPageDto(page);
    }

    IPage<MetaDataDto> convertToPageDto(Page<?> page) {
        List<MetaDataDto> records = new ArrayList<>();
        page.getRecords().forEach(r -> {
            MetaDataDto metaDataDto = new MetaDataDto();
            BeanCopierUtils.copy(r, metaDataDto);
            records.add(metaDataDto);
        });

        IPage<MetaDataDto> pageDto = new Page<>();
        pageDto.setCurrent(page.getCurrent());
        pageDto.setRecords(records);
        pageDto.setSize(page.getSize());
        pageDto.setTotal(page.getTotal());
        return pageDto;
    }
}
