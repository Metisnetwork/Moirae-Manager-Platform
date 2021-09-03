package com.platon.rosettaflow.controller;

import cn.hutool.core.bean.BeanUtil;
import com.platon.rosettaflow.mapper.domain.ProjectTemp;
import com.platon.rosettaflow.req.project.temp.AddProjTempReq;
import com.platon.rosettaflow.service.IProjectTempService;
import com.platon.rosettaflow.vo.ResponseVo;
import com.platon.rosettaflow.vo.project.ProjTempListVo;
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
 * @author admin
 * @date 2021/8/16
 */
@Slf4j
@RestController
@Api(tags = "项目模板相关接口")
@RequestMapping(value = "project", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProjectTempController {

    @Resource
    private IProjectTempService projectTempService;

    @GetMapping("queryProjTempList")
    @ApiOperation(value = "查询项目模板列表", notes = "查询项目模板列表")
    public ResponseVo<List<ProjTempListVo>> queryProjectTempList() {
        List<ProjectTemp> list  = projectTempService.queryProjectTempList();
        return ResponseVo.createSuccess(BeanUtil.copyToList(list, ProjTempListVo.class));
    }

    @PostMapping("addProjTemp")
    @ApiOperation(value = "添加项目模板", notes = "添加项目模板")
    public ResponseVo<?> addProjTemp(@RequestBody @Valid AddProjTempReq addProjTempReq) {
        projectTempService.addProjTemp(addProjTempReq.getId());
        return ResponseVo.createSuccess();
    }

}
