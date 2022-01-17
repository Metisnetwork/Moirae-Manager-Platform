package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "t_workflow_run_task_result")
public class WorkflowRunTaskResult implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 项目工作流ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 所属组织
     */
    private String identityId;
    /**
     * 任务结果文件对应的任务Id
     */
    private String taskId;
    /**
     * 任务结果文件的名称
     */
    private String fileName;
    /**
     * 任务结果文件的元数据Id <系统默认生成的元数据>
     */
    private String metadataId;
    /**
     * 任务结果文件的原始文件Id
     */
    private String originId;
    /**
     * 任务结果文件的完整相对路径名
     */
    private String filePath;
    /**
     * 任务结果文件所在的 数据服务内网ip
     */
    private String ip;
    /**
     * 任务结果文件所在的 数据服务内网port
     */
    private String port;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private String identityName;
}
