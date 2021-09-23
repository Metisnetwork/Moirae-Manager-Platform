package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.mapper.domain.ProjectMember;

import java.util.List;

/**
 * @author admin
 * @date 2021/8/16
 */
public interface IProjectMemberService extends IService<ProjectMember> {

    /**
     * 根据项目id查询项目成员
     *
     * @param projectId 项目id
     * @return 项目成员列表
     */
    List<ProjectMember> queryByProjectId(Long projectId);

    /**
     * 获取项目管理员列表
     *
     * @param projectId 项目id
     * @return 管理员列表
     */
    List<ProjectMember> getAdminList(Long projectId);
}
