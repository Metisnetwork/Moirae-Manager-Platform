package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.PsiManager;
import com.moirae.rosettaflow.mapper.PsiMapper;
import com.moirae.rosettaflow.mapper.domain.Psi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PsiManagerImpl extends ServiceImpl<PsiMapper, Psi> implements PsiManager {
}
