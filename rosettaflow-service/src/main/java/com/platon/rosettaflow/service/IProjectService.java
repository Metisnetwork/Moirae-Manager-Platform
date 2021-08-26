package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.dto.ProjectDto;
import com.platon.rosettaflow.dto.ProjectTemplateDto;
import com.platon.rosettaflow.mapper.domain.Project;

import java.util.List;

/**
 * @author admin
 * @date 2021/8/16
 * @description 功能描述
 */
public interface IProjectService extends IService<Project> {

    /**
     * 保存项目
     * @param project
     */
    void saveProject(Project project);

    /**
     * 查询项目列表-分页
     * @param userId
     * @param projectName
     * @param pageNumber
     * @param pageSize
     * @return
     */
    List<ProjectDto>  queryProjectList(Long userId, String projectName, Integer pageNumber, Integer pageSize);

    /**
     * 查询项目详情
     * @param id
     * @return
     */
    Project queryProjectDetails(Long id);

}
