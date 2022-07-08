package com.datum.platform.mapper.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 提案信息
 * </p>
 *
 * @author chendai
 * @since 2022-07-08
 */
@Data
@TableName("mo_proposal")
public class Proposal implements Serializable {


    /**
     * 提案id
     */
    @TableId
    private String id;

    /**
     * 提案提交者
     */
    private String submitter;

    /**
     * 提案关联者
     */
    private String candidate;

    /**
     * 提案类型, 1-增加委员会成员; 2-剔除委员会成员; 3-委员会成员退出
     */
    @TableField("`type`")
    private Integer type;

    /**
     * 提案的附言
     */
    private String remark;

    /**
     * 提案的公示信息
     */
    private String publicityId;

    /**
     * 提交时间
     */
    private Date submissionTime;

    /**
     * 投票开始时间
     */
    private Date voteBeginTime;

    /**
     * 投票结束时间
     */
    private Date voteEndTime;

    /**
     * 赞成票数量
     */
    private Integer voteAgreeNumber;

    /**
     * 委员总数
     */
    private Integer authorityNumber;

    /**
     * 提案状态, 0-未开始; 1-投票中; 2-投票通过; 3-投票未通过; 4-退出中；5-已退出
     */
    @TableField("`status`")
    private Integer status;

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
