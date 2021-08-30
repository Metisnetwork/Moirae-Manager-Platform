package com.platon.rosettaflow.controller;

import cn.hutool.core.bean.BeanUtil;
import com.platon.rosettaflow.dto.ProjectDto;
import com.platon.rosettaflow.mapper.domain.Project;
import com.platon.rosettaflow.mapper.domain.ProjectTemp;
import com.platon.rosettaflow.req.project.ProjDetailsReq;
import com.platon.rosettaflow.req.project.ProjListReq;
import com.platon.rosettaflow.req.project.SaveProjectReq;
import com.platon.rosettaflow.service.IProjectService;
import com.platon.rosettaflow.vo.ResponseVo;
import com.platon.rosettaflow.vo.algorithm.AlgDetailsVo;
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
    @ApiOperation(value = "新增项目信息", notes = "新增项目信息")
    public ResponseVo<AlgDetailsVo> addProject(@RequestBody @Valid SaveProjectReq saveProjectReq) {
        projectService.addProject(BeanUtil.copyProperties(saveProjectReq, Project.class));
        return ResponseVo.createSuccess();
    }
    @PostMapping("updateProject")
    @ApiOperation(value = "修改项目信息", notes = "修改项目信息")
    public ResponseVo<AlgDetailsVo> saveProject(@RequestBody @Valid SaveProjectReq saveProjectReq) {
        projectService.updateProject(BeanUtil.copyProperties(saveProjectReq, Project.class));
        return ResponseVo.createSuccess();
    }

    @GetMapping("queryProjectList")
    @ApiOperation(value = "查询项目列表", notes = "查询项目列表")
    public ResponseVo<List<ProjectListVo>> queryProjectList(@Valid ProjListReq projListReq) {
        List<ProjectDto> list  = projectService.queryProjectList(projListReq.getUserId(),
                projListReq.getProjectName(), projListReq.getPageNumber(), projListReq.getPageSize());
        return ResponseVo.createSuccess(BeanUtil.copyToList(list, ProjectListVo.class));
    }

    @GetMapping("queryProjectDetails")
    @ApiOperation(value = "查询项目详情", notes = "查询项目详情")
    public ResponseVo<ProjectDetailsVo> queryProjectDetails(@Valid ProjDetailsReq projDetailsReq) {
        Project project  = projectService.queryProjectDetails(projDetailsReq.getId());
        return ResponseVo.createSuccess(BeanUtil.copyProperties(project, ProjectDetailsVo.class));
    }

    @GetMapping("queryProjectTempList")
    @ApiOperation(value = "查询模板项目列表", notes = "查询模板项目列表")
    public ResponseVo<List<ProjTempListVo>> queryProjectTempList() {
        List<ProjectTemp> list  = projectService.queryProjectTempList();
        return ResponseVo.createSuccess(BeanUtil.copyToList(list, ProjTempListVo.class));
    }

}
