package com.moirae.rosettaflow.controller;

import cn.hutool.core.bean.BeanUtil;
import com.moirae.rosettaflow.mapper.domain.ProjectTemp;
import com.moirae.rosettaflow.req.project.temp.AddProjectTemplateReq;
import com.moirae.rosettaflow.service.IProjectTempService;
import com.moirae.rosettaflow.vo.ResponseVo;
import com.moirae.rosettaflow.vo.project.ProjTempListVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 项目模板相关接口
 *
 * @author admin
 * @date 2021/8/16
 */
@Slf4j
@RestController
@Api(tags = "项目模板相关接口")
@RequestMapping(value = "projectTemplate", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProjectTempController {

    @Resource
    private IProjectTempService projectTempService;

    @GetMapping("list")
    @ApiOperation(value = "查询项目模板列表", notes = "查询项目模板列表")
    public ResponseVo<List<ProjTempListVo>> list() {
        List<ProjectTemp> list = projectTempService.projectTempList();
        return ResponseVo.createSuccess(BeanUtil.copyToList(list, ProjTempListVo.class));
    }

    @PostMapping("add")
    @ApiOperation(value = "添加项目模板", notes = "添加项目模板")
    public ResponseVo<?> add(@RequestBody @Valid AddProjectTemplateReq dddProjectTemplateReq) {
        projectTempService.addProjectTemplate(dddProjectTemplateReq.getId());
        return ResponseVo.createSuccess();
    }

}
