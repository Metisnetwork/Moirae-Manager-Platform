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
import com.platon.rosettaflow.mapper.domain.Project;
import com.platon.rosettaflow.mapper.domain.ProjectMember;
import com.platon.rosettaflow.service.IProjectMemberService;
import com.platon.rosettaflow.service.IProjectService;
import com.platon.rosettaflow.service.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    ProjectMemberMapper projectMemberMapper;

    @Resource
    IProjectMemberService projectMemberService;

    @Override
    public void addProject(Project project) {
        try {
            Long userId = UserContext.get().getId();
            if (userId == null ||  userId == 0L) {
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.USER_CACHE_LOST_ERROR.getMsg());
            }
            project.setUserId(userId);
            this.save(project);
        }catch (Exception e) {
            log.error("addProject--新增项目信息失败, 错误信息:{}", e.getMessage());
            if (e instanceof DuplicateKeyException){
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.PROJECT_NAME_EXISTED.getMsg());
            }
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.ADD_PROJ_ERROR.getMsg());
        }
    }

    @Override
    public void updateProject(Project project) {
        try {
            this.updateById(project);
        } catch (Exception e) {
            log.error("updateProject--修改项目信息失败, 错误信息:{}", e.getMessage());
            if (e instanceof DuplicateKeyException){
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.PROJECT_NAME_EXISTED.getMsg());
            }
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
            LambdaQueryWrapper<Project> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.eq(Project::getId, id);
            queryWrapper.eq(Project::getStatus, 1);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            log.error("queryProjectDetails--查询项目详情失败, 错误信息:{}", e.getMessage());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.QUERY_PROJ_DETAILS_ERROR.getMsg());
        }

    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteProject(Long id) {
        // 逻辑删除项目信息,修改项目版本标识，解决逻辑删除唯一校验问题
        this.updateBatchById(updateDelVersionById(Collections.singletonList(id)));
        // 根据项目id获取成员id
        List<Long> idList = getMemberIdByProjectId(id);
        // 批量逻辑删除项目成员，并修改项目成员版本标识
        projectMemberService.updateBatchById(idList);
    }

    /**  根据项目id获取成员id */
    private List<Long> getMemberIdByProjectId(Long projectId) {
        List<ProjectMember> projectMemberList = projectMemberService.queryByProjectId(projectId);
        if (projectMemberList == null || projectMemberList.size() == 0) {
            return new ArrayList<>();
        }
        List<Long> idList = new ArrayList<>();
        projectMemberList.forEach(projectMember -> idList.add(projectMember.getId()));
        return idList;
    }

    /** 修改版本标识，解决逻辑删除唯一校验问题 */
    private List<Project> updateDelVersionById(List<Long> idList){
        List<Project> projectList = new ArrayList<>();
        if (idList != null && idList.size() > 0) {
            idList.forEach(id -> {
                Project project = new Project();
                project.setId(id);
                project.setDelVersion(id);
                project.setStatus((byte)0);
                projectList.add(project);
            });
        }
        return projectList;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteProjectBatch(String ids) {
        // 转换id类型
        List<Long> idsList = convertIdType(ids);
        // 删除项目成员
        LambdaQueryWrapper<ProjectMember> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(ProjectMember::getProjectId, idsList);
        List<ProjectMember> projectMemberList = projectMemberMapper.selectList(queryWrapper);
        List<Long> memberIdsList = new ArrayList<>();
        if (projectMemberList != null && projectMemberList.size() > 0) {
            for (ProjectMember projectMember : projectMemberList) {
                memberIdsList.add(projectMember.getId());
            }
            // 逻辑删除项目成员，并修改项目成员版本标识
            projectMemberService.updateBatchById(memberIdsList);
        }
        // 逻辑删除项目信息，修改版本标识，解决逻辑删除唯一校验问题
        this.updateBatchById(updateDelVersionById(idsList));
    }

    @Override
    public IPage<ProjMemberDto> queryProjMemberList(Long projectId, String userName, Long current, Long size) {
       IPage<ProjMemberDto> iPage = new Page<>(current, size);
        return projectMemberMapper.queryProjMemberList(projectId, userName, iPage);
    }

    @Override
    public void addProjMember(ProjectMember projectMember) {
        try {
            projectMemberMapper.insert(projectMember);
        } catch (DuplicateKeyException e) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.MEMBER_ROLE_EXISTED.getMsg());
        }
    }

    @Override
    public void updateProjMember(ProjectMember projectMember) {
        try {
            projectMemberMapper.updateById(projectMember);
        } catch (DuplicateKeyException e) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.MEMBER_ROLE_EXISTED.getMsg());
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteProjMember(Long projMemberId) {
        // 逻辑删除项目成员，修改项目成员版本标识
        projectMemberService.updateBatchById(Collections.singletonList(projMemberId));
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteProjMemberBatch(String  projMemberIds) {
        List<Long> idList = convertIdType(projMemberIds);
        // 批量逻辑删除项目成员，并修改项目成员版本标识
        projectMemberService.updateBatchById(idList);
    }

    /** 转换id类型 */
    private List<Long> convertIdType(String ids){
        String[] idsArr = ids.split(",");
        return Arrays.stream(idsArr).map(id ->Long.parseLong(id.trim())).collect(Collectors.toList());
    }
}
