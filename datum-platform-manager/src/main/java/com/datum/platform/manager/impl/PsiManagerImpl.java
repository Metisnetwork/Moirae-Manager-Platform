package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.PsiManager;
import com.datum.platform.mapper.PsiMapper;
import com.datum.platform.mapper.domain.Psi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PsiManagerImpl extends ServiceImpl<PsiMapper, Psi> implements PsiManager {
}
