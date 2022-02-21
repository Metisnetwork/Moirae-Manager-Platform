package com.moirae.rosettaflow.dto;

import com.moirae.rosettaflow.mapper.domain.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class TaskDto extends Task {

    /**
     * 组织在任务中角色-任务发起方，当这些值>=1时表示是该角色
     */
    private Integer taskSponsor;

    /**
     * 组织在任务中角色-算法提供方，当这些值>=1时表示是该角色
     */
    private Integer algoProvider;

    /**
     * 组织在任务中角色-结果接收方，当这些值>=1时表示是该角色
     */
    private Integer resultConsumer;

    /**
     * 组织在任务中角色-数据提供方，当这些值>=1时表示是该角色
     */
    private Integer dataProvider;

    /**
     * 组织在任务中角色-算力提供方，当这些值>=1时表示是该角色
     */
    private Integer powerProvider;

    /**
     * 任务发起者
     */
    private Org sponsor;

    /**
     * 任务算法提供者
     */
    private TaskAlgoProvider taskAlgoProvider;

    /**
     * 任务结果接收者 (TODO)
     */
    private List<TaskResultConsumer> taskResultReceiverList;

    /**
     * 任务算法提供者
     */
    private List<TaskPowerProvider> taskPowerProviderList;

    /**
     * 任务数据提供者
     */
    private List<TaskDataProvider> taskDataProviderList;
}
