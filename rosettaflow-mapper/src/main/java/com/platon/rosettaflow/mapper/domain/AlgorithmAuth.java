package com.platon.rosettaflow.mapper.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * t_algorithm_auth
 * @author 
 */
@Data
@TableName(value = "t_algorithm_auth")
public class AlgorithmAuth implements Serializable {
    /**
     * 算法授权表ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 算法表id
     */
    private Long algorithmId;

    /**
     * 授权方式: 1-按时间, 2-按次数, 3-永久
     */
    private Byte authType;

    /**
     * 授权值: 按次数单位为（次）
     */
    private Long authValue;

    /**
     * 授权开始时间
     */
    private Date authBeginTime;

    /**
     * 授权结束时间
     */
    private Date authEndTime;

    /**
     * 授权状态: 0-待申请,1-申请中, 2-已授权,3-已拒绝
     */
    private Byte authStatus;

    /**
     * 状态: 0-无效，1- 有效
     */
    private Byte status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}