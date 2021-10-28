package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.mapper.AlgorithmAuthMapper;
import com.moirae.rosettaflow.mapper.domain.AlgorithmAuth;
import com.moirae.rosettaflow.service.IAlgorithmAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author hudenian
 * @date 2021/8/31
 * @description 算法授权服务实现类
 */
@Slf4j
@Service
public class AlgorithmAuthServiceImpl extends ServiceImpl<AlgorithmAuthMapper, AlgorithmAuth> implements IAlgorithmAuthService {
}
