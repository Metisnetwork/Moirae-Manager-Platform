package com.platon.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platon.rosettaflow.mapper.domain.ProjectTemplate;

public interface ProjectTemplateMapper extends BaseMapper<ProjectTemplate> {
    int deleteByPrimaryKey(Long id);

    int insert(ProjectTemplate record);

    int insertSelective(ProjectTemplate record);

    ProjectTemplate selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProjectTemplate record);

    int updateByPrimaryKey(ProjectTemplate record);
}