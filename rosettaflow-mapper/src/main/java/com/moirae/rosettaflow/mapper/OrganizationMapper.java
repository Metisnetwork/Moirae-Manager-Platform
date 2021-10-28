package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moirae.rosettaflow.mapper.domain.Organization;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author admin
 */
public interface OrganizationMapper extends BaseMapper<Organization> {

    /**
     * 批量插入机构信息
     *
     * @param organizationList 机构数据列表
     * @return 插入记录数
     */
    int batchInsert(@Param("organizationList") List<Organization> organizationList);
}