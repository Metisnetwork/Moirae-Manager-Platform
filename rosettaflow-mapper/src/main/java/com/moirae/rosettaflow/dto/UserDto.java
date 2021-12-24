package com.moirae.rosettaflow.dto;

import com.moirae.rosettaflow.mapper.domain.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author admin
 * @date 2021/8/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserDto extends User {

    private String token;
    /**
     * 用户绑定的组织的身份标识Id
     */
    private String identityId;
}
