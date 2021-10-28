package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.ProjectTemp;

import java.util.List;

/**
 * @author admin
 * @date 2021/8/16
 */
public interface IProjectTempService extends IService<ProjectTemp> {

    /**
     * 查询项目模板列表
     *
     * @return ProjectTemp
     */
    List<ProjectTemp> projectTempList();

    /**
     * 根据已创建的项目工作流添加项目模板
     *
     * @param workflowId 工作流id
     */
    void addProjectTemplate(Long workflowId);

    /**
     * 清空项目模板表
     */
    void truncate();
}
