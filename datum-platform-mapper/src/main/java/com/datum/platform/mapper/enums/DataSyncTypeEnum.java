package com.datum.platform.mapper.enums;

import lombok.Getter;

/**
 * @Author liushuyu
 * @Date 2022/1/5 12:14
 * @Version
 * @Desc
 */

@Getter
public enum DataSyncTypeEnum {

    ORG_STATUS("org_status", "同步组织状态"),
    META_DATA("meta_data", "同步元数据信息"),
    TASK("task","同步组织相关任务信息"),
    POWER("power", "算力同步"),
    ANALYZE_TASK("analyze_task","任务数据分析"),
    ANALYZE_PROPOSAL("analyze_proposal","提案数据分析")
    ;

    DataSyncTypeEnum(String dataType, String desc) {
        this.dataType = dataType;
        this.desc = desc;
    }

    private String dataType;
    private String desc;
}
