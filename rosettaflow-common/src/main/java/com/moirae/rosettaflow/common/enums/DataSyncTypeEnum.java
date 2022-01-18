package com.moirae.rosettaflow.common.enums;

import lombok.Getter;

/**
 * @Author liushuyu
 * @Date 2022/1/5 12:14
 * @Version
 * @Desc
 */

@Getter
public enum DataSyncTypeEnum {

    DATA_AUTH("data_auth", "同步数据授权信息"),
    META_DATA("meta_data", "同步元数据信息"),
    SUB_JOB_NODE_STATUS("sub_job_node_status", "同步子作业节点状态"),
    ORG_IDENTITY("org_identity","同步组织身份信息"),
    TASK("task_","同步组织相关任务信息");

    DataSyncTypeEnum(String dataType, String desc) {
        this.dataType = dataType;
        this.desc = desc;
    }

    private String dataType;
    private String desc;
}
