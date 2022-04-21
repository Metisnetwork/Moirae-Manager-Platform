package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.moirae.rosettaflow.mapper.enums.TaskStatusEnum;
import com.moirae.rosettaflow.mapper.enums.UserTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * dc_task
 * @author
 */
@Data
@TableName(value = "dc_task")
public class Task implements Serializable {
    /**
     * 任务ID,hash
     */
    @TableId
    private String id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 发起任务的用户的信息 (task是属于用户的)
     */
    @TableField("user_id")
    private String address;

    /**
     * 用户类型 (0: 未定义; 1: 以太坊地址; 2: Alaya地址; 3: PlatON地址
     */
    private UserTypeEnum userType;

    /**
     * 需要的内存, 字节
     */
    private Long requiredMemory;

    /**
     * 需要的core
     */
    private Integer requiredCore;

    /**
     * 需要的带宽, bps
     */
    private Long requiredBandwidth;

    /**
     * 需要的时间, milli seconds
     */
    private Long requiredDuration;

    /**
     * 任务创建者组织身份ID
     */
    private String ownerIdentityId;

    /**
     * 任务参与方在本次任务中的唯一识别ID
     */
    private String ownerPartyId;

    /**
     * 任务创建时间，精确到毫秒
     */
    private Date createAt;

    /**
     * 任务开始执行时间，精确到毫秒
     */
    private Date startAt;

    /**
     * 任务结束时间，精确到毫秒
     */
    private Date endAt;

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
     * 使用的所有数据大小，字节
     */
    private Long usedFileSize;

    /**
     * 任务状态, 0:未知;1:等待中;2:计算中,3:失败;4:成功
     */
    private TaskStatusEnum status;

    /**
     * 任务状态说明
     */
    private String statusDesc;

    /**
     * 任务描述
     */
    private String remarks;

    /**
     * 任务签名
     */
    private String taskSign;

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
     * 该组织在任务中角色-是否任务发方
     */
    @TableField(exist = false)
    private Boolean isTaskSponsor;

    /**
     * 该组织在任务中角色-是否算法提供方
     */
    @TableField(exist = false)
    private Boolean isAlgoProvider;

    /**
     * 该组织在任务中角色-是否数据提供方
     */
    @TableField(exist = false)
    private Boolean isDataProvider;

    /**
     * 该组织在任务中角色-是否算力提供方
     */
    @TableField(exist = false)
    private Boolean isPowerProvider;

    /**
     * 该组织在任务中角色-是否结果接收方
     */
    @TableField(exist = false)
    private Boolean isResultReceiver;

    /**
     * 任务发起方
     */
    @TableField(exist = false)
    private Org sponsor;

    /**
     * 算法提供方
     */
    @TableField(exist = false)
    private TaskAlgoProvider algoProvider;

    /**
     * 数据提供方
     */
    @TableField(exist = false)
    private List<TaskDataProvider> dataProviderList;
    /**
     * 算力提供方
     */
    @TableField(exist = false)
    private List<TaskPowerProvider> powerProviderList;
    /**
     * 结果接收方
     */
    @TableField(exist = false)
    private List<TaskResultConsumer> resultReceiverList;
    /**
     * 任务事件
     */
    @TableField(exist = false)
    private List<TaskEvent> eventList;
    @TableField(exist = false)
    private Date statsTime;
    @TableField(exist = false)
    private Integer taskCount;
    @TableField(exist = false)
    private Integer addressCount;
    @TableField(exist = false)
    private Integer dataCount;
}
