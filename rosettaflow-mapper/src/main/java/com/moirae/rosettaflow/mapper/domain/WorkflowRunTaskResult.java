package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 工作流任务运行状态
 * </p>
 *
 * @author chendai
 * @since 2022-03-28
 */
@Data
@TableName("mo_workflow_run_task_result")
public class WorkflowRunTaskResult implements Serializable {


    /**
     * ID(自增长)
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属组织
     */
    @TableField("identity_id")
    private String identityId;

    /**
     * 任务ID,底层处理完成后返回
     */
    @TableField("task_id")
    private String taskId;

    /**
     * 任务结果文件的名称
     */
    @TableField("file_name")
    private String fileName;

    /**
     * 任务结果文件的元数据Id <系统默认生成的元数据>
     */
    @TableField("metadata_id")
    private String metadataId;

    /**
     * 任务结果文件的原始文件Id
     */
    @TableField("origin_id")
    private String originId;

    /**
     * 任务结果文件的完整相对路径名
     */
    @TableField("file_path")
    private String filePath;

    /**
     * 任务结果文件所在的 数据服务内网ip
     */
    private String ip;

    /**
     * 任务结果文件所在的 数据服务内网port
     */
    @TableField("`port`")
    private String port;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(update = "now()")
    private Date updateTime;

    private static final long serialVersionUID = 1L;


}
