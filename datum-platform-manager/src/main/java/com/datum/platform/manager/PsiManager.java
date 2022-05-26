package com.datum.platform.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datum.platform.mapper.domain.Psi;

import java.util.List;

public interface PsiManager extends IService<Psi> {
    List<Psi> listByTrainTaskId(String taskId);
}
