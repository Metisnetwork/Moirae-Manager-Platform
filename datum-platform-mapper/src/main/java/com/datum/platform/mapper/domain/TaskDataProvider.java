package com.datum.platform.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * dc_task_data_provider
 * @author
 */
@Data
@TableName(value = "dc_task_data_provider")
public class TaskDataProvider implements Serializable {
    /**
     * 任务ID,hash
     */
    private String taskId;

    /**
     * 参与任务的元数据ID
     */
    private String metaDataId;

    /**
     * 元数据名称
     */
    private String metaDataName;

    /**
     * 存储的策略类型. 0: 未知原始数据格式， 1: csv格式原始数据， 2: dir格式 (目录)， 3: binary格式 (普通的二进制数据, 没有明确说明后缀的二进制文件)
     */
    private Integer policyType;

    /**
     * 输入数据的类型. 0: unknown, 1: origin_data, 2: model
     */
    private Integer inputType;

    /**
     * (冗余)参与任务的元数据的所属组织的identity_id
     */
    private String identityId;

    /**
     * 任务参与方在本次任务中的唯一识别ID
     */
    private String partyId;

    /**
     * 元数据在此次任务中的主键列下标索引序号
     */
    private Integer keyColumnIdx;

    /**
     * 元数据选择列
     */
    private String selectedColumns;

    private static final long serialVersionUID = 1L;

    /**
     * 组织名称
     */
    @TableField(exist = false)
    private String nodeName;

    /**
     * 元数据代币名称
     */
    @TableField(exist = false)
    private String dataTokenName;
}
