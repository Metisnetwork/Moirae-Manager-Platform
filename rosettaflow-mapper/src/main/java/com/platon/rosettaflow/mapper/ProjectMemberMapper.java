package com.platon.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platon.rosettaflow.dto.ProjMemberDto;
import com.platon.rosettaflow.mapper.domain.ProjectMember;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  项目成员mapper类
 * @author houz
 */
public interface ProjectMemberMapper extends BaseMapper<ProjectMember> {

    /**
     * * 查询项目成员列表
     * @param projectId 项目id
     * @param userName 用户昵称
     * @param iPage 分页
     * @return
     */
    List<ProjMemberDto> queryProjMemberList(@Param(value = "projectId")Long projectId,
                                            @Param(value = "userName")String userName,
                                            IPage<ProjMemberDto> iPage);
}