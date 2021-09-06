package com.platon.rosettaflow.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author juzix
 * @description 获取登录nonce返回对象
 */
@Data
@AllArgsConstructor
@ApiModel("获取登录nonce参数")
public class NonceVo {
    @ApiModelProperty("登录随机数")
    private String nonce;

}
