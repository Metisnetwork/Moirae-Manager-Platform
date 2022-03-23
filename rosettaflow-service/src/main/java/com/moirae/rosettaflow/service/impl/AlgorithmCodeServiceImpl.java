package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.mapper.AlgorithmCodeMapper;
import com.moirae.rosettaflow.mapper.domain.AlgorithmCode;
import com.moirae.rosettaflow.service.IAlgorithmCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 算法实现类
 * @author admin
 * @date 2021/8/23
 */
@Slf4j
@Service
public class AlgorithmCodeServiceImpl extends ServiceImpl<AlgorithmCodeMapper, AlgorithmCode> implements IAlgorithmCodeService {

}
