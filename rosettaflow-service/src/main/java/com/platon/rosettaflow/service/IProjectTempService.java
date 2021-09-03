package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.mapper.domain.ProjectTemp;

import java.util.List;

/**
 * @author admin
 * @date 2021/8/16
 */
public interface IProjectTempService extends IService<ProjectTemp> {

    /**
     * 查询项目模板列表
     * @return ProjectTemp
     */
    List<ProjectTemp> queryProjectTempList();

    /**
     * 添加项目模板
     * @param projTempId 项目模板id
     */
    void addProjTemp(Long projTempId);


}
