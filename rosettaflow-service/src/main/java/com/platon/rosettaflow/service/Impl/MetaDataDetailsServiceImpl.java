package com.platon.rosettaflow.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.utils.BeanCopierUtils;
import com.platon.rosettaflow.dto.MetaDataDetailsDto;
import com.platon.rosettaflow.mapper.MetaDataDetailsMapper;
import com.platon.rosettaflow.mapper.domain.MetaDataDetails;
import com.platon.rosettaflow.service.IMetaDataDetailsService;
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
public class MetaDataDetailsServiceImpl extends ServiceImpl<MetaDataDetailsMapper, MetaDataDetails> implements IMetaDataDetailsService {
    @Override
    public void truncate() {
        this.baseMapper.truncate();
    }

    @Override
    public List<MetaDataDetailsDto> findByMetaDataId(String metaDataId) {
        LambdaQueryWrapper<MetaDataDetails> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MetaDataDetails::getMetaDataId, metaDataId);
        List<MetaDataDetails> metaDataDetailsList = this.list(wrapper);
        return this.convertToListDto(metaDataDetailsList);
    }

    private List<MetaDataDetailsDto> convertToListDto(List<MetaDataDetails> poList) {
        List<MetaDataDetailsDto> dtoList = new ArrayList<>();
        poList.forEach(po -> {
            MetaDataDetailsDto m = new MetaDataDetailsDto();
            BeanCopierUtils.copy(po, m);
            dtoList.add(m);
        });
        return dtoList;
    }
}
