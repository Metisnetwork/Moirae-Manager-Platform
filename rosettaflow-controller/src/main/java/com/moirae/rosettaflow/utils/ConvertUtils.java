package com.moirae.rosettaflow.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moirae.rosettaflow.common.enums.ErrorMsg;
import com.moirae.rosettaflow.common.enums.RespCodeEnum;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.dto.AlgorithmDto;
import com.moirae.rosettaflow.dto.WorkflowNodeDto;
import com.moirae.rosettaflow.mapper.domain.*;
import com.moirae.rosettaflow.req.workflow.expert.WorkflowNodeReq;
import com.moirae.rosettaflow.vo.PageVo;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author admin
 * @date 2021/8/16
 * @description 功能描述
 */
public class ConvertUtils {

    public static <T> PageVo<T> convertPageVo(IPage<?> page, List<T> items) {
        PageVo<T> pageVo = new PageVo<>();
        pageVo.setCurrent(page.getCurrent());
        pageVo.setItems(items);
        pageVo.setSize(page.getSize());
        pageVo.setTotal(page.getTotal());
        return pageVo;
    }

    /**
     * 列表对象并行转换工具(性能好，会乱序)
     *
     * @param list1  源列表数据
     * @param tClass 目标类
     * @return 目标类列表
     */
    @SuppressWarnings("unused")
    public static <T> List<T> convertParallelToList(List<?> list1, Class<T> tClass) {
        List<T> list2 = new ArrayList<>();
        list1.parallelStream().forEach(o1 -> {
            T target = ReflectUtil.newInstanceIfPossible(tClass);
            BeanUtils.copyProperties(o1, target);
            list2.add(target);
        });
        return list2;
    }

    /** 转换保存请求参数 (是否是保存接口调用（checkFlag:保存节点调用时，无需校验输入输出数据）)*/
    public static List<WorkflowNodeDto> convertSaveReq(List<WorkflowNodeReq> workflowNodeReqList, boolean checkFlag) {
        if(null == workflowNodeReqList || workflowNodeReqList.size() == 0) {
            return new ArrayList<>();
        }
        List<WorkflowNodeDto> workflowNodeDtoList = new ArrayList<>();
        for (WorkflowNodeReq workflowNodeReq : workflowNodeReqList) {
            WorkflowNodeDto workflowNodeDto = new WorkflowNodeDto();
            // 节点输入
            if (workflowNodeReq.getWorkflowNodeInputReqList() != null
                    && workflowNodeReq.getWorkflowNodeInputReqList().size() > 0) {
                workflowNodeDto.setWorkflowNodeInputList(BeanUtil.copyToList(
                        workflowNodeReq.getWorkflowNodeInputReqList(), WorkflowNodeInput.class));
            }
            // 节点输出
            if (workflowNodeReq.getWorkflowNodeOutputReqList() != null
                    && workflowNodeReq.getWorkflowNodeOutputReqList().size() > 0) {
                workflowNodeDto.setWorkflowNodeOutputList(BeanUtil.copyToList(
                        workflowNodeReq.getWorkflowNodeOutputReqList(), WorkflowNodeOutput.class));
            }
            // 节点算法代码
            if (Objects.nonNull(workflowNodeReq.getWorkflowNodeCodeReq())) {
                workflowNodeDto.setWorkflowNodeCode(BeanUtil.toBean(
                        workflowNodeReq.getWorkflowNodeCodeReq(), WorkflowNodeCode.class));
            }
            // 节点环境资源
            if (Objects.nonNull(workflowNodeReq.getWorkflowNodeResourceReq())) {
                workflowNodeDto.setWorkflowNodeResource(BeanUtil.toBean(
                        workflowNodeReq.getWorkflowNodeResourceReq(), WorkflowNodeResource.class));
            }
            // 节点输入变量
            if (workflowNodeReq.getWorkflowNodeVariableReqList() != null
                    && workflowNodeReq.getWorkflowNodeVariableReqList().size() > 0) {
                workflowNodeDto.setWorkflowNodeVariableList(BeanUtil.copyToList(
                        workflowNodeReq.getWorkflowNodeVariableReqList(), WorkflowNodeVariable.class));
            }
            // 工作流id
            //TODO
//            workflowNodeDto.setWorkflowId(workflowNodeReq.getWorkflowId());
            // 节点算法id
            workflowNodeDto.setAlgorithmId(workflowNodeReq.getAlgorithmId());
            // 节点名称
            workflowNodeDto.setNodeName(workflowNodeReq.getNodeName());
            // 节点步骤
            workflowNodeDto.setNodeStep(workflowNodeReq.getNodeStep());
            // 模型id
            workflowNodeDto.setModelId(workflowNodeReq.getModelId());
            // 是否需要输入模型: 0-否，1:是
            AlgorithmDto algorithmDto = new AlgorithmDto();
            algorithmDto.setInputModel(workflowNodeReq.getInputModel().byteValue());
            workflowNodeDto.setAlgorithmDto(algorithmDto);
            // 校验工作流节点配置参数
            if (checkFlag) {
                checkNodeParam(workflowNodeDto);
            }
            workflowNodeDtoList.add(workflowNodeDto);
        }
        return workflowNodeDtoList;
    }

    /** 校验工作流节点配置参数(是否是保存接口调用（checkFlag:保存节点调用时，无需校验输入输出数据）) */
    private static void checkNodeParam(WorkflowNodeDto workflowNodeDto) {
        if (null == workflowNodeDto.getWorkflowNodeInputList() || workflowNodeDto.getWorkflowNodeInputList().size() == 0) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NODE_NOT_INPUT_EXIST.getMsg());
        }
        if (null == workflowNodeDto.getWorkflowNodeOutputList() || workflowNodeDto.getWorkflowNodeOutputList().size() == 0) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NODE_NOT_OUTPUT_EXIST.getMsg());
        }
    }

}
