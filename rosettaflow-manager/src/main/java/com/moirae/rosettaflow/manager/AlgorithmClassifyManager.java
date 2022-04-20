package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.AlgorithmClassify;

import java.util.List;

public interface AlgorithmClassifyManager extends IService<AlgorithmClassify> {
    List<AlgorithmClassify> listByParentId(Long parentId);
}
