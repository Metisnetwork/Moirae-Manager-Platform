package com.platon.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platon.rosettaflow.mapper.domain.Project;

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
     * @param pageNumber
     * @param pageSize
     */
    List<Project> queryProjectList(Long userId, String projectName, Integer pageNumber, Integer pageSize);



}