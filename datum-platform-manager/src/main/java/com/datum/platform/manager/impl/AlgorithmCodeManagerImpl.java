package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.AlgorithmCodeManager;
import com.datum.platform.mapper.AlgorithmCodeMapper;
import com.datum.platform.mapper.domain.AlgorithmCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AlgorithmCodeManagerImpl extends ServiceImpl<AlgorithmCodeMapper, AlgorithmCode> implements AlgorithmCodeManager {
}
