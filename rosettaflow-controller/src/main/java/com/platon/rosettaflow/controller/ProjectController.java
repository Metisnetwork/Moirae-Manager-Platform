package com.platon.rosettaflow.controller;

import com.platon.rosettaflow.mapper.domain.Project;
import com.platon.rosettaflow.req.project.ProjListReq;
import com.platon.rosettaflow.req.project.SaveProjectReq;
import com.platon.rosettaflow.service.IProjectService;
import com.platon.rosettaflow.utils.ConvertUtils;
import com.platon.rosettaflow.vo.ResponseVo;
import com.platon.rosettaflow.vo.algorithm.AlgDetailsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author admin
 * @date 2021/8/16
 * @description 项目模板管理
 */
@Slf4j
@RestController
@Api(tags = "项目管理关接口")
@RequestMapping(value = "project", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProjectController {

    @Resource
    private IProjectService projectService;

    @PostMapping("saveProject")
    @ApiOperation(value = "保存项目信息", notes = "保存项目信息")
    public ResponseVo<AlgDetailsVo> saveProject(@RequestBody @Valid SaveProjectReq saveProjectReq) {
        try {
            Project project = (Project)ConvertUtils.convertToVo(saveProjectReq, new Project());
            projectService.saveProject(project);
            return ResponseVo.createSuccess();
        } catch (Exception e) {
            log.error("project--saveProject--保存项目信息失败, 错误信息:{}", e);
            return ResponseVo.createFail();
        }
    }

    @PostMapping("queryProjectList")
    @ApiOperation(value = "查询项目列表", notes = "查询项目列表")
    public ResponseVo<AlgDetailsVo> queryProjectList(@RequestBody @Valid ProjListReq projListReq) {
        try {
            projectService.queryProjectList(projListReq.getUserId(),
                    projListReq.getProjectName(), projListReq.getPageNumber(), projListReq.getPageSize());
            return ResponseVo.createSuccess();
        } catch (Exception e) {
            log.error("project--queryProjectList--查询项目列表失败, 错误信息:{}", e);
            return ResponseVo.createFail();
        }
    }

}
