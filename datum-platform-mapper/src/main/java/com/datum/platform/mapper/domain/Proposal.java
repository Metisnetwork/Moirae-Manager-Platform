package com.datum.platform.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.datum.platform.mapper.enums.ProposalStatusEnum;
import com.datum.platform.mapper.enums.ProposalTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

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
    private ProposalTypeEnum type;

    /**
     * 提案的公示信息
     */
    private String publicityId;

    /**
     * 提交时间
     */
    private String submissionBn;

    /**
     * 投票开始时间
     */
    private String voteBeginBn;

    /**
     * 投票结束时间
     */
    private String voteEndBn;

    /**
     * 主动退出的块高
     */
    private String autoQuitBn;

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
    private ProposalStatusEnum status;

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
     * 提交的组织id
     */
    @TableField(exist = false)
    private String submitterIdentityId;
    /**
     * 提交的组织名称
     */
    @TableField(exist = false)
    private String submitterNodeName;
    /**
     * 候选的组织id
     */
    @TableField(exist = false)
    private String candidateIdentityId;
    /**
     * 候选的组织名称
     */
    @TableField(exist = false)
    private String candidateNodeName;
    /**
     * 投票开始时间
     */
    @TableField(exist = false)
    private Date voteBeginTime;
    /**
     * 投票结束时间
     */
    @TableField(exist = false)
    private Date voteEndTime;
    /**
     * 提交时间
     */
    @TableField(exist = false)
    private Date submissionTime;
    /**
     * 提案公式信息
     */
    @TableField(exist = false)
    private Publicity publicity;
}
