package com.platon.rosettaflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.enums.StatusEnum;
import com.platon.rosettaflow.dto.MetaDataDetailsDto;
import com.platon.rosettaflow.mapper.MetaDataDetailsMapper;
import com.platon.rosettaflow.mapper.domain.MetaDataDetails;
import com.platon.rosettaflow.service.IMetaDataDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/24
 * @description 元数据服务实现类
 */
@Slf4j
@Service
public class MetaDataDetailsServiceImpl extends ServiceImpl<MetaDataDetailsMapper, MetaDataDetails> implements IMetaDataDetailsService {
    @Override
    public void truncate() {
        this.baseMapper.truncate();
    }

    @Override
    public IPage<MetaDataDetailsDto> findByMetaDataId(String metaDataId, Long current, Long size) {
        Page<MetaDataDetails> page = new Page<>(current, size);
        LambdaQueryWrapper<MetaDataDetails> wrapper = getQueryWrapper(metaDataId, null);
        this.page(page, wrapper);
        return this.convertToPageDto(page);
    }

    @Override
    public IPage<MetaDataDetailsDto> findById(Long id, Long current, Long size) {

        Page<MetaDataDetails> page = new Page<>(current, size);
        LambdaQueryWrapper<MetaDataDetails> wrapper = getQueryWrapper(null, id);
        this.page(page, wrapper);
        return this.convertToPageDto(page);
    }

    @Override
    public List<MetaDataDetailsDto> getAllAuthColumns(String metaDataId) {
        LambdaQueryWrapper<MetaDataDetails> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MetaDataDetails::getMetaDataId, metaDataId);
        wrapper.eq(MetaDataDetails::getStatus, StatusEnum.VALID.getValue());
        wrapper.orderByAsc(MetaDataDetails::getColumnIndex);
        List<MetaDataDetails> metaDataDetailsList = this.list(wrapper);
        return BeanUtil.copyToList(metaDataDetailsList, MetaDataDetailsDto.class);
    }

    @Override
    public int batchInsert(List<MetaDataDetails> metaDataDetailsList) {
        return this.baseMapper.batchInsert(metaDataDetailsList);
    }

    private LambdaQueryWrapper<MetaDataDetails> getQueryWrapper(String metaDataId, Long id) {
        LambdaQueryWrapper<MetaDataDetails> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MetaDataDetails::getStatus, StatusEnum.VALID.getValue());
        if (StrUtil.isNotBlank(metaDataId)) {
            wrapper.eq(MetaDataDetails::getMetaDataId, metaDataId);
        }
        if (id != null) {
            wrapper.eq(MetaDataDetails::getId, id);
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
