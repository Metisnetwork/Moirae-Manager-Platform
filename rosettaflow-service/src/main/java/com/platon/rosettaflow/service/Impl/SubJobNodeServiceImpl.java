package com.platon.rosettaflow.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.mapper.SubJobNodeMapper;
import com.platon.rosettaflow.mapper.domain.SubJobNode;
import com.platon.rosettaflow.service.ISubJobNodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author hudenian
 * @date 2021/8/27
 * @description 功能描述
 */
@Slf4j
@Service
public class SubJobNodeServiceImpl extends ServiceImpl<SubJobNodeMapper, SubJobNode> implements ISubJobNodeService {
}
