package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.Psi;

import java.util.List;

public interface PsiManager extends IService<Psi> {
    List<Psi> listByTrainTaskId(String taskId);
}
