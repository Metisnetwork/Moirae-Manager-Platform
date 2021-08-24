package com.platon.rosettaflow.controller;

import com.platon.rosettaflow.dto.ProjectTemplateDto;
import com.platon.rosettaflow.service.IProjectTemplateService;
import com.platon.rosettaflow.utils.ConvertUtils;
import com.platon.rosettaflow.vo.ResponseVo;
import com.platon.rosettaflow.vo.projectTemplate.ProjectTemplateVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author admin
 * @date 2021/8/16
 * @description 项目模板管理
 */
@Slf4j
@RestController
@Api(tags = "项目模板管理关接口")
@RequestMapping(value = "project", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProjectTemplateController {

    @Resource
    private IProjectTemplateService projectTemplate;

    @GetMapping("list")
    @ApiOperation(value = "项目模板列表", notes = "项目模板列表")
    public ResponseVo<List<ProjectTemplateVo>> list() {
        List<ProjectTemplateDto> projectTemplateDtoList = projectTemplate.getAllTemplate();
        return ResponseVo.createSuccess(ConvertUtils.convert2Vo(projectTemplateDtoList));
    }

}
