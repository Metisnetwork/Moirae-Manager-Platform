package com.platon.rosettaflow.mapper;

import com.platon.rosettaflow.mapper.domain.ProjectMember;

public interface ProjectMemberMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProjectMember record);

    int insertSelective(ProjectMember record);

    ProjectMember selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProjectMember record);

    int updateByPrimaryKey(ProjectMember record);
}