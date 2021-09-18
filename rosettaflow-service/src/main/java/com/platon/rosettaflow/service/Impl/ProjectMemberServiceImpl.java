package com.platon.rosettaflow.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.enums.StatusEnum;
import com.platon.rosettaflow.mapper.ProjectMemberMapper;
import com.platon.rosettaflow.mapper.domain.ProjectMember;
import com.platon.rosettaflow.service.IProjectMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 * @date 2021/8/16
 */
@Slf4j
@Service
public class ProjectMemberServiceImpl extends ServiceImpl<ProjectMemberMapper, ProjectMember> implements IProjectMemberService {


    @Override
    public List<ProjectMember> queryByProjectId(Long projectId) {
        LambdaQueryWrapper<ProjectMember> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ProjectMember::getProjectId, projectId);
        queryWrapper.eq(ProjectMember::getStatus, StatusEnum.VALID.getValue());
        return this.list(queryWrapper);
    }
}
