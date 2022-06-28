package com.datum.platform.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.datum.platform.mapper.enums.TokenTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * token信息
 * </p>
 *
 * @author chendai
 * @since 2022-03-28
 */
@Data
@TableName("mo_token")
public class Token implements Serializable {


    /**
     * 合约地址
     */
    @TableId
    private String address;

    /**
     * 合约类型: 0-erc20,1-erc721
     */
    private TokenTypeEnum type;

    /**
     * 对应元数据ID,hash
     */
    private String metaDataId;

    /**
     * 合约名称
     */
    private String name;

    /**
     * 合约符号
     */
    private String symbol;

    /**
     * 合约精度
     */
    @TableField("`decimal`")
    private Long decimal;

    /**
     * erc20对应LAT的价格
     */
    private String price;

    /**
     * erc20是否添加流动性: 0-否，1-是
     */
    private Boolean isAddLiquidity;

    /**
     * 明文算法消耗量
     */
    private String ptAlgConsume;

    /**
     * 密文算法消耗量
     */
    private String ctAlgConsume;

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
