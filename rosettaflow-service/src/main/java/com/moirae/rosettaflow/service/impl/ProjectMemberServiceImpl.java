package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.common.enums.ProjectMemberRoleEnum;
import com.moirae.rosettaflow.common.enums.StatusEnum;
import com.moirae.rosettaflow.mapper.ProjectMemberMapper;
import com.moirae.rosettaflow.mapper.domain.ProjectMember;
import com.moirae.rosettaflow.service.IProjectMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author admin
 * @date 2021/8/16
 */
@Slf4j
@Service
public class ProjectMemberServiceImpl extends ServiceImpl<ProjectMemberMapper, ProjectMember> implements IProjectMemberService {

    @Override
    public ProjectMember queryById(Long id) {
        LambdaQueryWrapper<ProjectMember> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ProjectMember::getId, id);
        queryWrapper.eq(ProjectMember::getStatus, StatusEnum.VALID.getValue());
        return this.getOne(queryWrapper);
    }

    @Override
    public List<ProjectMember> queryByProjectId(Long projectId) {
        LambdaQueryWrapper<ProjectMember> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ProjectMember::getProjectId, projectId);
        queryWrapper.eq(ProjectMember::getStatus, StatusEnum.VALID.getValue());
        return this.list(queryWrapper);
    }

    @Override
    public ProjectMember queryByProjectIdAndUserId(Long userId, Long projectId) {
        LambdaQueryWrapper<ProjectMember> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ProjectMember::getProjectId, projectId);
        queryWrapper.eq(ProjectMember::getUserId, userId);
        queryWrapper.eq(ProjectMember::getStatus, StatusEnum.VALID.getValue());
        return this.getOne(queryWrapper);
    }

    @Override
    public List<ProjectMember> getAdminList(Long projectId) {
        LambdaQueryWrapper<ProjectMember> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ProjectMember::getProjectId, projectId);
        wrapper.eq(ProjectMember::getRole, ProjectMemberRoleEnum.ADMIN.getRoleId());
        wrapper.eq(ProjectMember::getStatus, StatusEnum.VALID.getValue());
        return this.list(wrapper);
    }

    @Override
    public void deleteMemberByProjectId(List<Long> projectId) {
        LambdaQueryWrapper<ProjectMember> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ProjectMember::getProjectId, projectId);
        this.remove(queryWrapper);
    }
}
