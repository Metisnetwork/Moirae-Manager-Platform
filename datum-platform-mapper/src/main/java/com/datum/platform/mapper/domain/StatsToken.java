package com.datum.platform.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "mo_stats_token")
public class StatsToken implements Serializable {

    /**
     * 凭证地址
     */
    @TableId
    private String address;

    /**
     * 数据凭证使用量
     */
    private Long tokenUsed;

    private static final long serialVersionUID = 1L;
    @TableField(exist = false)
    private String tokenAddress;
    @TableField(exist = false)
    private String tokenName;
}
