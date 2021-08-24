package com.platon.rosettaflow.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.enums.StatusEnum;
import com.platon.rosettaflow.dto.ProjectTemplateDto;
import com.platon.rosettaflow.mapper.ProjectTemplateMapper;
import com.platon.rosettaflow.mapper.domain.ProjectTemplate;
import com.platon.rosettaflow.service.IProjectTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 * @date 2021/8/16
 * @description 功能描述
 */
@Slf4j
@Service
public class ProjectTemplateImpl extends ServiceImpl<ProjectTemplateMapper, ProjectTemplate> implements IProjectTemplateService {

    public static List<ProjectTemplateDto> convert2Dto(List<ProjectTemplate> projectTemplateList) {
        List<ProjectTemplateDto> list = new ArrayList<>();
        ProjectTemplateDto projectTemplateDto;

        for (ProjectTemplate projectTemplate : projectTemplateList) {
            projectTemplateDto = new ProjectTemplateDto();
            BeanUtils.copyProperties(projectTemplate, projectTemplateDto);
            list.add(projectTemplateDto);
        }
        return list;
    }

    @Override
    public List<ProjectTemplateDto> getAllTemplate() {
        LambdaQueryWrapper<ProjectTemplate> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ProjectTemplate::getStatus, StatusEnum.VALID.getValue());
        List<ProjectTemplate> projectTemplateList = this.list(wrapper);
        return convert2Dto(projectTemplateList);
    }
}
