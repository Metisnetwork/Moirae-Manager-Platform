package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * dc_power_server
 * @author
 */
@Data
@TableName(value = "dc_power_server")
public class PowerServer implements Serializable {
    /**
     * 计算服务主机ID,hash
     */
    @TableId
    private String id;

    /**
     * 组织身份ID
     */
    private String identityId;

    /**
     * 计算服务内存, 字节
     */
    private Long memory;

    /**
     * 计算服务core
     */
    private Integer core;

    /**
     * 计算服务带宽, bps
     */
    private Long bandwidth;

    /**
     * 使用的内存, 字节
     */
    private Long usedMemory;

    /**
     * 使用的core
     */
    private Integer usedCore;

    /**
     * 使用的带宽, bps
     */
    private Long usedBandwidth;

    /**
     * 是否发布，true/false
     */
    private Boolean published;

    /**
     * 发布时间，精确到毫秒
     */
    private Date publishedAt;

    /**
     * 算力的状态 (0: 未知; 1: 还未发布的算力; 2: 已发布的算力(算力未被占用); 3: 已发布的算力(算力正在被占用); 4: 已撤销的算力)
     */
    private Integer status;

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
}
