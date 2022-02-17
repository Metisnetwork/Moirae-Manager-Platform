package com.moirae.rosettaflow.dto;

import com.moirae.rosettaflow.mapper.enums.OrgStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_organization
 *
 * @author admin
 */
@Data
public class OrganizationDto implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 组织表ID(自增长)
     */
    private Long id;
    /**
     * 组织的身份名称
     */
    private String nodeName;
    /**
     * 组织中调度服务的 nodeId
     */
    private String nodeId;
    /**
     * 组织的身份标识Id
     */
    private String identityId;
    /**
     * 组织的ip
     */
    private String identityIp;
    /**
     * 组织的端口
     */
    private Integer identityPort;

    /**
     * 是否公有可查节点: 0-否，1- 是
     */
    private Integer publicFlag;
    /**
     * 状态: 0-未知，1- 正常， 2- 异常
     */
    private OrgStatusEnum status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否为默认的链接设置：0-否，1-是
     */
    private Byte defaultConnectFlag;

    private Date updateAt;

    /**
     * 总文件数
     */
    private Integer totalFile;

    /**
     * 总文件大小(字节)
     */
    private Long totalData;
}
