package com.platon.rosettaflow.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.mapper.AlgorithmAuthMapper;
import com.platon.rosettaflow.mapper.domain.AlgorithmAuth;
import com.platon.rosettaflow.service.IAlgorithmAuthService;
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
