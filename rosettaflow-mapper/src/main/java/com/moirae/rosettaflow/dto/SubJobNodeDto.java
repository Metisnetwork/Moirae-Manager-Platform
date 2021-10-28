package com.moirae.rosettaflow.dto;

import com.moirae.rosettaflow.mapper.domain.SubJobNode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author juzix
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SubJobNodeDto extends SubJobNode {

    /**
     * 工作流id
     */
    private Long workflowId;
    /**
     * 节点数
     */
    private Integer nodeNumber;
}
