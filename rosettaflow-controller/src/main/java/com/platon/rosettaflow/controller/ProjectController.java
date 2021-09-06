package com.platon.rosettaflow.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platon.rosettaflow.dto.ProjMemberDto;
import com.platon.rosettaflow.dto.ProjectDto;
import com.platon.rosettaflow.mapper.domain.Project;
import com.platon.rosettaflow.mapper.domain.ProjectMember;
import com.platon.rosettaflow.mapper.domain.ProjectTemp;
import com.platon.rosettaflow.req.project.*;
import com.platon.rosettaflow.service.IProjectService;
import com.platon.rosettaflow.utils.ConvertUtils;
import com.platon.rosettaflow.vo.PageVo;
import com.platon.rosettaflow.vo.ResponseVo;
import com.platon.rosettaflow.vo.project.ProjMemberListVo;
import com.platon.rosettaflow.vo.project.ProjTempListVo;
import com.platon.rosettaflow.vo.project.ProjectDetailsVo;
import com.platon.rosettaflow.vo.project.ProjectListVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 项目管理关接口
 * @author admin
 * @date 2021/8/16
 */
@Slf4j
@RestController
@Api(tags = "项目管理关接口")
@RequestMapping(value = "project", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProjectController {

    @Resource
    private IProjectService projectService;

    @GetMapping("queryProjectList")
    @ApiOperation(value = "查询项目列表", notes = "查询项目列表")
    public ResponseVo<PageVo<ProjectListVo>> queryProjectList(@Valid ProjListReq projListReq) {
        IPage<ProjectDto> iPage  = projectService.queryProjectList(projListReq.getProjectName(), projListReq.getCurrent(), projListReq.getSize());
        List<ProjectListVo> items = BeanUtil.copyToList(iPage.getRecords(), ProjectListVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(iPage, items));
    }

    @GetMapping("queryProjectDetails")
    @ApiOperation(value = "查询项目详情", notes = "查询项目详情")
    public ResponseVo<ProjectDetailsVo> queryProjectDetails(@Valid ProjDetailsReq projDetailsReq) {
        Project project  = projectService.queryProjectDetails(projDetailsReq.getId());
        return ResponseVo.createSuccess(BeanUtil.copyProperties(project, ProjectDetailsVo.class));
    }

    @PostMapping("addProject")
    @ApiOperation(value = "新增项目", notes = "新增项目")
    public ResponseVo<?> addProject(@RequestBody @Valid AddProjectReq addProjectReq) {
        projectService.addProject(BeanUtil.copyProperties(addProjectReq, Project.class));
        return ResponseVo.createSuccess();
    }
    @PostMapping("updateProject")
    @ApiOperation(value = "修改项目", notes = "修改项目")
    public ResponseVo<?> updateProject(@RequestBody @Valid UpdateProjectReq updateProjectReq) {
        projectService.updateProject(BeanUtil.copyProperties(updateProjectReq, Project.class));
        return ResponseVo.createSuccess();
    }

    @PostMapping("deleteProject")
    @ApiOperation(value = "删除项目", notes = "删除项目")
    public ResponseVo<?> deleteProject(@RequestBody @Valid ProjDetailsReq projDetailsReq) {
        projectService.deleteProject(projDetailsReq.getId());
        return ResponseVo.createSuccess();
    }

    @PostMapping("deleteProjectBatch")
    @ApiOperation(value = "批量删除项目", notes = "批量删除项目")
    public ResponseVo<?> deleteProjectBatch(@RequestBody @Valid DeleteProjBatchReq deleteBatchReq) {
        projectService.deleteProjectBatch(deleteBatchReq.getIds());
        return ResponseVo.createSuccess();
    }

    @GetMapping("queryProjMemberList")
    @ApiOperation(value = "查询项目成员列表", notes = "查询项目成员列表")
    public ResponseVo<PageVo<ProjMemberListVo>> queryProjMemberList(@Valid ProjMemberListReq listReq) {
        IPage<ProjMemberDto> iPage  = projectService.queryProjMemberList(listReq.getProjectId(),
                listReq.getUserName(), listReq.getCurrent(), listReq.getSize());
        List<ProjMemberListVo> items = BeanUtil.copyToList(iPage.getRecords(), ProjMemberListVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(iPage, items));
    }

    @PostMapping("addProjMember")
    @ApiOperation(value = "新增项目成员", notes = "新增项目成员")
    public ResponseVo<?> addProjMember(@RequestBody @Valid ProjMemberReq projMemberReq) {
        ProjectMember projectMember = BeanUtil.toBean(projMemberReq, ProjectMember.class);
        projectService.addProjMember(projectMember);
        return ResponseVo.createSuccess();
    }

    @PostMapping("updateProjMember")
    @ApiOperation(value = "修改项目成员", notes = "修改项目成员")
    public ResponseVo<?> updateProjMember(@RequestBody @Valid ProjMemberReq projMemberReq) {
        ProjectMember projectMember = BeanUtil.toBean(projMemberReq, ProjectMember.class);
        projectService.updateProjMember(projectMember);
        return ResponseVo.createSuccess();
    }

    @PostMapping("deleteProjMember")
    @ApiOperation(value = "删除项目成员", notes = "删除项目成员")
    public ResponseVo<?> deleteProjMember(@RequestBody @Valid DeleteMemberReq deleteMemberReq) {
        projectService.deleteProjMember(deleteMemberReq.getProjMemberId());
        return ResponseVo.createSuccess();
    }

    @PostMapping("deleteProjMemberBatch")
    @ApiOperation(value = "批量删除项目成员", notes = "批量删除项目成员")
    public ResponseVo<?> deleteProjMemberBatch(@RequestBody @Valid DeleteMemberBatchReq deleteBatchReq) {
        projectService.deleteProjMemberBatch(deleteBatchReq.getProjMemberIds());
        return ResponseVo.createSuccess();
    }

}
