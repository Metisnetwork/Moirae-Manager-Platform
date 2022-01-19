package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.ProjectMember;

import java.util.List;

/**
 * @author admin
 * @date 2021/8/16
 */
public interface IProjectMemberService extends IService<ProjectMember> {

    /**
     * 根据项目成员id查询项目成员
     *
     * @param id 项目成员id
     * @return ProjectMember
     */
    ProjectMember queryById(Long id);

    /**
     * 根据项目id和用户id查询项目成员
     *
     * @param userId    用户id
     * @param projectId 项目id
     * @return ProjectMember
     */
    ProjectMember queryByProjectIdAndUserId(Long userId, Long projectId);

    /**
     * 获取项目管理员列表
     *
     * @param projectId 项目id
     * @return 管理员列表
     */
    List<ProjectMember> getAdminList(Long projectId);

    /**
     * 物理删除，根据项目id删除项目所属成员
     *
     * @param projectId 项目id
     */
    void deleteMemberByProjectId(List<Long> projectId);
}
