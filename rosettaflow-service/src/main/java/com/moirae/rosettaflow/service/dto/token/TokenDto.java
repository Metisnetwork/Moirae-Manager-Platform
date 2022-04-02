package com.moirae.rosettaflow.service.dto.token;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "ERC20代币信息")
public class TokenDto {

    @ApiModelProperty(value = "合约地址")
    private String address;

    @ApiModelProperty(value = "合约名称")
    private String name;

    @ApiModelProperty(value = "合约符号")
    private String symbol;

    @ApiModelProperty(value = "合约精度")
    private Integer decimal;
}
