package com.platon.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platon.rosettaflow.dto.ProjectDto;
import com.platon.rosettaflow.mapper.domain.Project;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目mapper类
 * @author houz
 */
public interface ProjectMapper extends BaseMapper<Project> {

    /**
     * 查询项目列表-分页
     * @param userId
     * @param projectName
     * @param page
     * @return
     */
    IPage<ProjectDto> queryProjectList(@Param(value = "userId")Long userId,
                                      @Param(value = "projectName")String projectName,
                                      IPage<ProjectDto> page);



}