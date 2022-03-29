package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.moirae.rosettaflow.mapper.enums.OrgStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "dc_org")
public class Org implements Serializable {
    /**
     * 身份认证标识的id
     */
    @TableId
    private String identityId;

    /**
     * 组织身份名称
     */
    private String nodeName;

    /**
     * 组织节点ID
     */
    private String nodeId;

    /**
     * 组织机构图像url
     */
    private String imageUrl;

    /**
     * 组织机构简介
     */
    private String details;

    /**
     * 状态,1-Normal; 2-NonNormal
     */
    @TableField(value = "`status`")
    private OrgStatusEnum status;

    /**
     * (状态)修改时间
     */
    private Date updateAt;

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

    /**
     * 数据凭证使用量
     */
    @TableField(exist = false)
    private long dataTokenUsed;

    /**
     * 参与任务数量
     */
    @TableField(exist = false)
    private Integer totalTask;

    /**
     * 总文件数
     */
    @TableField(exist = false)
    private Integer totalFile;

    /**
     * 计算服务的总共内存
     */
    @TableField(exist = false)
    private Long totalMemory;

    /**
     * 计算服务的总带宽
     */
    @TableField(exist = false)
    private Long totalBandwidth;

    /**
     * 计算服务的总核数
     */
    @TableField(exist = false)
    private Integer totalCore;

}
