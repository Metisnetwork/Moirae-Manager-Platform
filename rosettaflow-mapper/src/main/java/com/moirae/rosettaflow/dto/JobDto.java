package com.moirae.rosettaflow.dto;

import com.moirae.rosettaflow.mapper.domain.Job;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hudenian
 * @date 2021/8/13
 * @description 调度任务dto
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JobDto  extends Job {

    private String workflowName;

}
