package com.moirae.rosettaflow.dto;

import com.moirae.rosettaflow.mapper.domain.WorkflowNodeOutput;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 功能描述
 * @author hudenian
 * @date 2021/8/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WorkflowNodeOutputDto extends WorkflowNodeOutput {

    /** 组织名称 */
    private String identityName;
}
