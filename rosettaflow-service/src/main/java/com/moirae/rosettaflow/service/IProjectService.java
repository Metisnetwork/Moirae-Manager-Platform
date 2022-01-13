package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.dto.ProjMemberDto;
import com.moirae.rosettaflow.dto.ProjectDto;
import com.moirae.rosettaflow.dto.ProjectModelDto;
import com.moirae.rosettaflow.mapper.domain.Project;
import com.moirae.rosettaflow.mapper.domain.ProjectMember;
import com.moirae.rosettaflow.mapper.domain.User;

import java.util.List;

/**
 * @author admin
 * @date 2021/8/16
 */
public interface IProjectService extends IService<Project> {

    /**
     * 新增项目
     *
     * @param projectDto 项目信息
     * @param language 语言类型
     * @return id 项目id
     */
    Long addProject(ProjectDto projectDto, String language);

    /**
     * 修改项目
     *
     * @param project 项目信息
     */
    void updateProject(Project project);

    /**
     * 查询项目列表-分页
     *
     * @param projectName 项目名称
     * @param current     当前页数
     * @param size        每页条数
     * @return ProjectDto
     */
    IPage<ProjectDto> queryProjectPageList(String projectName, Long current, Long size);

    /**
     * 查询项目详情
     *
     * @param id 项目id
     * @return Project
     */
    Project queryProjectDetails(Long id);

    /**
     * 删除项目
     *
     * @param id 项目id
     */
    void deleteProject(Long id);

    /**
     * 批量删除项目
     *
     * @param ids 项目id
     */
    void deleteProjectBatch(String ids);

    /**
     * 查询项目成员列表
     *
     * @param projectId   项目id
     * @param projectName 项目名称
     * @param current     当前页
     * @param size        页条数
     * @return IPage
     */
    IPage<ProjMemberDto> queryProjMemberPageList(Long projectId, String projectName, Long current, Long size);

    /**
     * 新增项目成员
     *
     * @param projectMember 项目成员
     */
    void addProjMember(ProjectMember projectMember);

    /**
     * 修改项目成员
     *
     * @param projectMember 项目成员
     */
    void updateProjMember(ProjectMember projectMember);

    /**
     * 删除项目成员
     *
     * @param projMemberId 项目成员id
     */
    void deleteProjMember(Long projMemberId);

    /**
     * 批量删除项目成员
     *
     * @param projMemberIds 项目成员id
     */
    void deleteProjMemberBatch(String projMemberIds);

    /**
     * 根据项目id获取项目角色
     *
     * @param projectId 项目id
     * @return 项目角色
     */
    Byte getRoleByProjectId(Long projectId);

    /**
     * 查询所有用户昵称
     *
     * @param projectId 项目id
     * @return 用户列表
     */
    List<User> queryAllUserNickName(Long projectId);
}
