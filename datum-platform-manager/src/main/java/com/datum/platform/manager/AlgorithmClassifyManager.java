package com.datum.platform.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datum.platform.mapper.domain.AlgorithmClassify;

import java.util.List;

public interface AlgorithmClassifyManager extends IService<AlgorithmClassify> {
    List<AlgorithmClassify> listByParentId(Long parentId);
}
