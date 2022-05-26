package com.datum.platform.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * token持有表
 * </p>
 *
 * @author chendai
 * @since 2022-03-28
 */
@Data
@TableName("mo_token_holder")
public class TokenHolder implements Serializable {


    /**
     * 合约地址
     */
    @TableId("token_address")
    private String tokenAddress;

    /**
     * 用户地址
     */
    private String address;

    /**
     * 地址代币余额, ERC20为金额
     */
    private String balance;

    /**
     * 地址已授权支付助手的代币余额, ERC20为金额
     */
    private String authorizeBalance;

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
