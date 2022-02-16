package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.OrgManager;
import com.moirae.rosettaflow.mapper.OrgMapper;
import com.moirae.rosettaflow.mapper.domain.Org;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrgManagerImpl extends ServiceImpl<OrgMapper, Org> implements OrgManager {

}
