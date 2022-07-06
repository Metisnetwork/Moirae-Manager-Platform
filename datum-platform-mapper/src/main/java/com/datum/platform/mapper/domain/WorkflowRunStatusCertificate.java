package com.datum.platform.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.datum.platform.mapper.enums.MetaDataCertificateTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 工作流运行使用的凭证
 * </p>
 *
 * @author chendai
 * @since 2022-03-28
 */
@Data
@TableName("mo_workflow_run_status_certificate")
public class WorkflowRunStatusCertificate implements Serializable {


    /**
     * ID(自增长)
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 工作流运行状态ID
     */
    private Long workflowRunId;

    /**
     * 对应元数据ID,hash
     */
    private String metaDataId;

    /**
     * 凭证类型
     */
    private MetaDataCertificateTypeEnum type;

    /**
     * 合约地址
     */
    private String tokenAddress;

    /**
     * 721下合约token id
     */
    private String tokenId;

    /**
     * 无属性凭证明文算法消耗量
     */
    private String erc20PtAlgConsume;

    /**
     * 无属性凭证密文算法消耗量
     */
    private String erc20CtAlgConsume;

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
    @TableField(exist = false)
    private List<WorkflowRunStatusTask> workflowRunTaskStatusList;
    @TableField(exist = false)
    private Workflow workflow;
}
