package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.AlgorithmManager;
import com.datum.platform.mapper.AlgorithmMapper;
import com.datum.platform.mapper.domain.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AlgorithmManagerImpl extends ServiceImpl<AlgorithmMapper, Algorithm> implements AlgorithmManager {
}
