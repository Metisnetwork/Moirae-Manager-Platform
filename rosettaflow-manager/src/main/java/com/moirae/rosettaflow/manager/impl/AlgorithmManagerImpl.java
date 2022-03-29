package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.AlgorithmManager;
import com.moirae.rosettaflow.mapper.AlgorithmMapper;
import com.moirae.rosettaflow.mapper.domain.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AlgorithmManagerImpl extends ServiceImpl<AlgorithmMapper, Algorithm> implements AlgorithmManager {
}
