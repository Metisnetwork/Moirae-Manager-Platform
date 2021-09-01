package com.platon.rosettaflow.controller;

import cn.hutool.core.bean.BeanUtil;
import com.platon.rosettaflow.dto.ProjMemberDto;
import com.platon.rosettaflow.dto.ProjectDto;
import com.platon.rosettaflow.mapper.domain.Project;
import com.platon.rosettaflow.mapper.domain.ProjectMember;
import com.platon.rosettaflow.mapper.domain.ProjectTemp;
import com.platon.rosettaflow.req.project.*;
import com.platon.rosettaflow.service.IProjectService;
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
 * 项目模板管理
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

    @PostMapping("addProject")
    @ApiOperation(value = "新增项目", notes = "新增项目")
    public ResponseVo<?> addProject(@RequestBody @Valid SaveProjectReq saveProjectReq) {
        projectService.addProject(BeanUtil.copyProperties(saveProjectReq, Project.class));
        return ResponseVo.createSuccess();
    }
    @PostMapping("updateProject")
    @ApiOperation(value = "修改项目", notes = "修改项目")
    public ResponseVo<?> saveProject(@RequestBody @Valid SaveProjectReq saveProjectReq) {
        projectService.updateProject(BeanUtil.copyProperties(saveProjectReq, Project.class));
        return ResponseVo.createSuccess();
    }

    @GetMapping("queryProjectList")
    @ApiOperation(value = "查询项目列表", notes = "查询项目列表")
    public ResponseVo<List<ProjectListVo>> queryProjectList(@Valid ProjListReq projListReq) {
        List<ProjectDto> list  = projectService.queryProjectList(projListReq.getProjectName(), projListReq.getCurrent(), projListReq.getSize());
        return ResponseVo.createSuccess(BeanUtil.copyToList(list, ProjectListVo.class));
    }

    @GetMapping("queryProjectDetails")
    @ApiOperation(value = "查询项目详情", notes = "查询项目详情")
    public ResponseVo<ProjectDetailsVo> queryProjectDetails(@Valid ProjDetailsReq projDetailsReq) {
        Project project  = projectService.queryProjectDetails(projDetailsReq.getId());
        return ResponseVo.createSuccess(BeanUtil.copyProperties(project, ProjectDetailsVo.class));
    }

    @PostMapping("deleteProject")
    @ApiOperation(value = "删除项目", notes = "修改项目")
    public ResponseVo<?> deleteProject(@RequestBody @Valid ProjDetailsReq projDetailsReq) {
        projectService.deleteProject(projDetailsReq.getId());
        return ResponseVo.createSuccess();
    }

    @GetMapping("queryProjectTempList")
    @ApiOperation(value = "查询模板项目列表", notes = "查询模板项目列表")
    public ResponseVo<List<ProjTempListVo>> queryProjectTempList() {
        List<ProjectTemp> list  = projectService.queryProjectTempList();
        return ResponseVo.createSuccess(BeanUtil.copyToList(list, ProjTempListVo.class));
    }

    @GetMapping("queryProjMemberList")
    @ApiOperation(value = "查询项目成员列表", notes = "查询项目成员列表")
    public ResponseVo<List<ProjMemberListVo>> queryProjMemberList(@Valid ProjMemberListReq projMemberListReq) {
        List<ProjMemberDto> list  = projectService.queryProjMemberList(
                projMemberListReq.getProjectId(), projMemberListReq.getUserName());
        return ResponseVo.createSuccess(BeanUtil.copyToList(list, ProjMemberListVo.class));
    }

    @PostMapping("addProjMember")
    @ApiOperation(value = "新增项目成员", notes = "新增项目成员")
    public ResponseVo<?> addProjMember(@Valid ProjMemberReq projMemberReq) {
        ProjectMember projectMember = BeanUtil.toBean(projMemberReq, ProjectMember.class);
        projectService.addProjMember(projectMember);
        return ResponseVo.createSuccess();
    }

    @PostMapping("updateProjMember")
    @ApiOperation(value = "修改项目成员", notes = "修改项目成员")
    public ResponseVo<?> updateProjMember(@Valid ProjMemberReq projMemberReq) {
        ProjectMember projectMember = BeanUtil.toBean(projMemberReq, ProjectMember.class);
        projectService.updateProjMember(projectMember);
        return ResponseVo.createSuccess();
    }

    @PostMapping("deleteProjMember")
    @ApiOperation(value = "删除项目成员", notes = "删除项目成员")
    public ResponseVo<?> deleteProjMember(@Valid DeleteMemberReq deleteMemberReq) {
        projectService.deleteProjMember(deleteMemberReq.getMemberId());
        return ResponseVo.createSuccess();
    }

}
