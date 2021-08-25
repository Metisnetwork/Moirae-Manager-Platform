package com.platon.rosettaflow.service.Impl;

import cn.hutool.poi.PoiChecker;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.enums.StatusEnum;
import com.platon.rosettaflow.dto.ProjectTemplateDto;
import com.platon.rosettaflow.mapper.ProjectMapper;
import com.platon.rosettaflow.mapper.domain.Project;
import com.platon.rosettaflow.service.IProjectService;
import com.platon.rosettaflow.service.IProjectTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 * @date 2021/8/16
 * @description 功能描述
 */
@Slf4j
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements IProjectService {

    @Resource
    ProjectMapper projectMapper;

    @Override
    public void saveProject(Project project) {
        if (null == project.getId() || project.getId() == 0) {
            this.save(project);
        }
        if (null != project.getId() || project.getId() > 0) {
            this.updateById(project);
        }
    }

    @Override
    public List<Project> queryProjectList(Long userId, String projectName, Integer pageNumber, Integer pageSize) {
//        Page<Project> page = new Page<>(pageNumber,pageSize);
        LambdaQueryChainWrapper<Project> queryWrapper = lambdaQuery();
        queryWrapper.eq(Project::getUserId, userId)
                .eq(Project::getProjectName, projectName)
                .last(" limit" + pageNumber + "," + pageSize);
        List list = this.list(queryWrapper);
        return null;
    }
}
