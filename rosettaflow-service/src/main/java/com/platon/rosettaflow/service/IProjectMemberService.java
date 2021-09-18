package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.dto.ProjMemberDto;
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
     * @param projectId
     * @return
     */
    List<ProjectMember> queryByProjectId(Long projectId);

}
