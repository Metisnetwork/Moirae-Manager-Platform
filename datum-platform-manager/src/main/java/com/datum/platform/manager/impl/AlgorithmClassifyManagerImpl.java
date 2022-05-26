package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.AlgorithmClassifyManager;
import com.datum.platform.mapper.AlgorithmClassifyMapper;
import com.datum.platform.mapper.domain.AlgorithmClassify;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AlgorithmClassifyManagerImpl extends ServiceImpl<AlgorithmClassifyMapper, AlgorithmClassify> implements AlgorithmClassifyManager  {
    @Override
    public List<AlgorithmClassify> listByParentId(Long parentId) {
        LambdaQueryWrapper<AlgorithmClassify> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(AlgorithmClassify::getParentId, parentId);
        return list(wrapper);
    }
}
