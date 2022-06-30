package com.datum.platform.service.dto.data;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "用户有属性凭证账户信息")
public class UserHaveAttributesCredentialDto extends HaveAttributesCredentialDto {

}
