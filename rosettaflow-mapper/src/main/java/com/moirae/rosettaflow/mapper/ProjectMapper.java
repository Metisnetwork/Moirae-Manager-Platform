package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moirae.rosettaflow.dto.ProjectDto;
import com.moirae.rosettaflow.dto.ProjectMemberDto;
import com.moirae.rosettaflow.mapper.domain.Project;
import org.apache.ibatis.annotations.Param;

/**
 * 项目mapper类
 *
 * @author houz
 */
public interface ProjectMapper extends BaseMapper<Project> {

    /**
     * 查询项目列表-分页
     *
     * @param userId      用户id
     * @param projectName 项目名称
     * @param page        分布
     * @return IPage
     */
    IPage<ProjectDto> queryProjectPageList(@Param(value = "userId") Long userId,
                                       @Param(value = "projectName") String projectName,
                                       IPage<ProjectDto> page);

    /**
     * * 查询项目成员列表
     *
     * @param projectId 项目id
     * @param userName  用户昵称
     * @param iPage     分页
     * @return IPage
     */
    IPage<ProjectMemberDto> queryProjMemberList(@Param(value = "projectId") Long projectId,
                                                @Param(value = "userName") String userName,
                                                IPage<ProjectMemberDto> iPage);
}
