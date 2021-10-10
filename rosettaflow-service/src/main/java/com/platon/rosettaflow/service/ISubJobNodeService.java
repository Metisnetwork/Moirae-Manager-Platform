package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.dto.SubJobNodeDto;
import com.platon.rosettaflow.mapper.domain.SubJobNode;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/27
 * @description 子作业节点管理服务接口
 */
public interface ISubJobNodeService extends IService<SubJobNode> {

    /**
     *  修改子作业节点运行状态
     * @param subJobId 子作业id
     * @param runStatus 运行状态
     */
    void updateRunStatusByJobId(Long subJobId, Byte runStatus);


    /**
     * 获取查询子作业节点信息
     * @param subJobId 子作业id
     * @return SubJobNode 子作业节点
     */
    SubJobNode querySubJobNodeByJobId(Long subJobId);


    /**
     * 获取查询子作业节点信息
     * @param subJobId 子作业id
     * @param nodeStep 节点在工作流中序号
     * @return SubJobNode 子作业节点
     */
    SubJobNode querySubJobNodeByJobIdAndNodeStep(Long subJobId,Integer nodeStep);


    /**
     * 获取所有子工作流运行中的节点
     *
     * @return 运行中节点列表
     */
    List<SubJobNodeDto> getRunningNodeWithWorkIdAndNodeNum();


    /**
     * 批量修改子作业节点状态
     *
     * @param ids 子作业ids
     * @param runStatus  运行状态
     */
    void updateRunStatus(Object[] ids, Byte runStatus);
}
