package com.platon.rosettaflow.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.mapper.SubJobMapper;
import com.platon.rosettaflow.mapper.domain.SubJob;
import com.platon.rosettaflow.service.ISubJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author hudenian
 * @date 2021/8/27
 * @description 子作业管理服务实现类
 */
@Slf4j
@Service
public class SubJobServiceImpl extends ServiceImpl<SubJobMapper, SubJob> implements ISubJobService {
}
