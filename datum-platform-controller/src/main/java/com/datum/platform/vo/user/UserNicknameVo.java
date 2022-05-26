package com.datum.platform.vo.user;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询所有用户昵称响应对象
 * @author houz
 */
@Data
@ApiModel(value = "查询所有用户昵称响应对象")
public class UserNicknameVo {

    @ApiModelProperty("用户ID")
    private Long id;

    @ApiModelProperty("用户昵称")
    private String userName;


}
