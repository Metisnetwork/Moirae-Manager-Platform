package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moirae.rosettaflow.dto.UserOrgMaintainDto;
import com.moirae.rosettaflow.dto.WorkflowDto;
import com.moirae.rosettaflow.mapper.domain.UserOrgMaintain;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author admin
 */
public interface UserOrgMaintainMapper extends BaseMapper<UserOrgMaintain> {
    /**
     * 查询当前用户维护的组织绑定信息关系表
     * @param address 用户钱包地址
     * @return
     */
    List<UserOrgMaintainDto> queryUserOrgMaintainPageList(String address);
}
