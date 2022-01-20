package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.common.constants.SysConstant;
import com.moirae.rosettaflow.common.enums.StatusEnum;
import com.moirae.rosettaflow.mapper.ProjectTempMapper;
import com.moirae.rosettaflow.mapper.domain.ProjectTemp;
import com.moirae.rosettaflow.service.IProjectTempService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author admin
 * @date 2021/8/16
 */
@Slf4j
@Service
public class ProjectTempServiceImpl extends ServiceImpl<ProjectTempMapper, ProjectTemp> implements IProjectTempService {

    @Override
    public List<ProjectTemp> projectTempList(String language) {
        LambdaQueryWrapper<ProjectTemp> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ProjectTemp::getStatus, StatusEnum.VALID.getValue());
        List<ProjectTemp> returnList = this.list(wrapper);
        // 处理国际化
        if (SysConstant.EN_US.equals(language)) {
            for (ProjectTemp ProjectTemp : returnList) {
                ProjectTemp.setProjectName(ProjectTemp.getProjectNameEn());
                ProjectTemp.setProjectDesc(ProjectTemp.getProjectDescEn());
            }
            return returnList;
        }
        return returnList;
    }
}
