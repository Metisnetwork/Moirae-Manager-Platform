package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.dto.ProjectDto;
import com.platon.rosettaflow.mapper.domain.Project;

import java.util.List;

/**
 * @author admin
 * @date 2021/8/16
 */
public interface IProjectService extends IService<Project> {

    /**
     * 新增项目
     * @param project 项目信息
     */
    void addProject(Project project);

    /**
     * 修改项目
     * @param project 项目信息
     */
    void updateProject(Project project);

    /**
     * 查询项目列表-分页
     * @param userId 用户id
     * @param projectName 项目名称
     * @param pageNumber 起始页数
     * @param pageSize 每页条数
     * @return ProjectDto
     */
    List<ProjectDto>  queryProjectList(Long userId, String projectName, Integer pageNumber, Integer pageSize);

    /**
     * 查询项目详情
     * @param id 项目id
     * @return Project
     */
    Project queryProjectDetails(Long id);

}
