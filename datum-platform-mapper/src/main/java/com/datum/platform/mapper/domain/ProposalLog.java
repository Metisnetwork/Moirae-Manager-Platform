package com.datum.platform.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.datum.platform.mapper.enums.ProposalLogTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 提案日志
 * </p>
 *
 * @author chendai
 * @since 2022-07-08
 */
@Data
@TableName("mo_proposal_log")
public class ProposalLog implements Serializable {


    /**
     * 日志表id(自增长)
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 事件类型, 1-提交提案; 2-撤销提案; 3-对提案投票; 4-投票结果
     */
    private ProposalLogTypeEnum type;

    /**
     * 事件所在块高
     */
    private String blockNumber;

    /**
     * 事件对应交易hash
     */
    private String txHash;

    /**
     * 事件日志index
     */
    private String logIndex;

    /**
     * 事件对应内容
     */
    private String content;

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
