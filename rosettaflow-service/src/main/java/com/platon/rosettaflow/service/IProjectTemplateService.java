package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.dto.ProjectTemplateDto;
import com.platon.rosettaflow.mapper.domain.ProjectTemplate;

import java.util.List;

/**
 * @author admin
 * @date 2021/8/16
 * @description 功能描述
 */
public interface IProjectTemplateService extends IService<ProjectTemplate> {
    /**
     * 获取所有本项目模板
     * @return 项目模板列表
     */
    List<ProjectTemplateDto> getAllTemplate();
}
