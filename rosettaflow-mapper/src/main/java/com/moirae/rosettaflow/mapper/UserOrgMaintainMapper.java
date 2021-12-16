package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moirae.rosettaflow.dto.UserOrgMaintainDto;
import com.moirae.rosettaflow.dto.WorkflowDto;
import com.moirae.rosettaflow.mapper.domain.UserOrgMaintain;
import org.apache.ibatis.annotations.Param;

/**
 * @author admin
 */
public interface UserOrgMaintainMapper extends BaseMapper<UserOrgMaintain> {
    /**
     * 查询当前用户维护的组织绑定信息关系表
     *
     * @param address 用户地址
     * @param orgName 组织名称
     * @param page    分页信息
     * @return 用户维护组织关系分页列表
     */
    IPage<UserOrgMaintainDto> queryUserOrgMaintainPageList(@Param("address") String address, @Param("orgName") String orgName, IPage<WorkflowDto> page);
}
