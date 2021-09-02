package com.platon.rosettaflow.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.enums.ErrorMsg;
import com.platon.rosettaflow.common.enums.RespCodeEnum;
import com.platon.rosettaflow.common.exception.BusinessException;
import com.platon.rosettaflow.dto.ProjMemberDto;
import com.platon.rosettaflow.dto.ProjectDto;
import com.platon.rosettaflow.mapper.ProjectMapper;
import com.platon.rosettaflow.mapper.ProjectMemberMapper;
import com.platon.rosettaflow.mapper.ProjectTempMapper;
import com.platon.rosettaflow.mapper.domain.Project;
import com.platon.rosettaflow.mapper.domain.ProjectMember;
import com.platon.rosettaflow.mapper.domain.ProjectTemp;
import com.platon.rosettaflow.service.IProjectService;
import com.platon.rosettaflow.service.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author admin
 * @date 2021/8/16
 */
@Slf4j
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements IProjectService {

    @Resource
    ProjectMapper projectMapper;

    @Resource
    ProjectTempMapper projectTempMapper;

    @Resource
    ProjectMemberMapper projectMemberMapper;

    @Override
    public void addProject(Project project) {
        try {
            Long userId = UserContext.get().getId();
            if (userId == null ||  userId == 0) {
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.USER_CACHE_LOST_ERROR.getMsg());
            }
            project.setUserId(userId);
            this.save(project);
        } catch (Exception e) {
            log.error("addProject--新增项目信息失败, 错误信息:{}", e.getMessage());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.ADD_PROJ_ERROR.getMsg());
        }
    }

    @Override
    public void updateProject(Project project) {
        try {
            this.updateById(project);
        } catch (Exception e) {
            log.error("updateProject--修改项目信息失败, 错误信息:{}", e.getMessage());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.UPDATE_PROJ_ERROR.getMsg());
        }
    }
    @Override
    public IPage<ProjectDto> queryProjectList(String projectName, Long current, Long size) {
       try {
           Long userId = UserContext.get().getId();
           if (userId == null ||  userId == 0) {
               throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.USER_CACHE_LOST_ERROR.getMsg());
           }
           IPage<ProjectDto> page = new Page<>(current, size);
           return projectMapper.queryProjectList(userId, projectName, page);
       } catch (Exception e) {
           log.error("queryProjectList--查询项目列表失败, 错误信息:{}", e.getMessage());
           throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.QUERY_PROJ_LIST_ERROR.getMsg());
       }

    }

    @Override
    public Project queryProjectDetails(Long id) {
        try {
            return this.getById(id);
        } catch (Exception e) {
            log.error("queryProjectDetails--查询项目详情失败, 错误信息:{}", e.getMessage());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.QUERY_PROJ_DETAILS_ERROR.getMsg());
        }

    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteProject(Long id) {
        // 删除项目信息
        this.removeById(id);
        // 删除项目成员
        LambdaQueryWrapper<ProjectMember> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ProjectMember::getProjectId, id);
        projectMemberMapper.delete(queryWrapper);
    }

    @Override
    public List<ProjectTemp> queryProjectTempList() {
        return projectTempMapper.selectList(null);
    }

    @Override
    public IPage<ProjMemberDto> queryProjMemberList(Long projectId, String userName, Long current, Long size) {
       IPage<ProjMemberDto> iPage = new Page<>(current, size);
        return projectMemberMapper.queryProjMemberList(projectId, userName, iPage);
    }

    @Override
    public void addProjMember(ProjectMember projectMember) {
        projectMemberMapper.insert(projectMember);
    }

    @Override
    public void updateProjMember(ProjectMember projectMember) {
        projectMemberMapper.updateById(projectMember);
    }

    @Override
    public void deleteProjMember(Long  projMemberId) {
        projectMemberMapper.deleteById(projMemberId);
    }
}
