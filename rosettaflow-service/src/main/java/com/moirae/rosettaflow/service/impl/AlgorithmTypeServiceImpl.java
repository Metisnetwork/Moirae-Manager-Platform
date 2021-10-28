package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.mapper.AlgorithmTypeMapper;
import com.moirae.rosettaflow.mapper.domain.AlgorithmType;
import com.moirae.rosettaflow.service.IAlgorithmTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author hudenian
 * @date 2021/10/14
 */
@Slf4j
@Service
public class AlgorithmTypeServiceImpl extends ServiceImpl<AlgorithmTypeMapper, AlgorithmType> implements IAlgorithmTypeService {
}
