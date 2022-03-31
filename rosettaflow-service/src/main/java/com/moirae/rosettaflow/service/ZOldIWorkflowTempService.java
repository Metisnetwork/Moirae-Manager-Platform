package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.ZOldWorkflowTemp;

/**
 * @author hudenian
 * @date 2021/9/7
 * @description 工作流模板服务
 */
public interface ZOldIWorkflowTempService extends IService<ZOldWorkflowTemp> {

    /**
     * 查询工作流模板
     * @param projectTempId 项目模板id
     * @return 工作流模板
     */
    ZOldWorkflowTemp getWorkflowTemplate(long projectTempId);
}
