package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.moirae.rosettaflow.mapper.enums.OrgStatusEnum;
import io.swagger.annotations.ApiModelProperty;
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
     * 参与任务数量
     */
    @TableField(exist = false)
    private Integer totalTask;
    /**
     * 总数据凭证数
     */
    @TableField(exist = false)
    private Integer totalDataToken;
    /**
     * 总数据数
     */
    @TableField(exist = false)
    private Integer totalData;
    /**
     * 计算服务的总共内存
     */
    @TableField(exist = false)
    private Long orgTotalMemory;
    /**
     * 计算服务的总带宽
     */
    @TableField(exist = false)
    private Long orgTotalBandwidth;
    /**
     * 计算服务的总核数
     */
    @TableField(exist = false)
    private Integer orgTotalCore;
    /**
     * 组织的ip
     */
    @TableField(exist = false)
    private String identityIp;
    /**
     * 组织的端口
     */
    @TableField(exist = false)
    private Integer identityPort;
    /**
     * 当前组织内置系统钱包地址 (见证人代理钱包)
     */
    @TableField(exist = false)
    private String observerProxyWalletAddress;
    /**
     * 是否公共可看的：0-否，1-是
     */
    @TableField(exist = false)
    private Byte publicFlag;
}
