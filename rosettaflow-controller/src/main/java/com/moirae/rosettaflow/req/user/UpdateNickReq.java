package com.moirae.rosettaflow.req.user;

import com.moirae.rosettaflow.common.utils.AddressChangeUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author admin
 * @date 2021/8/17
 */
@Data
@ApiModel("修改昵称请求参数")
public class UpdateNickReq {

    @ApiModelProperty(value = "昵称", required = true, example = "用户1")
    @NotBlank(message = "{user.nickname.notBlank}")
    private String nickName;
}
