package com.datum.platform.dto;

import com.datum.platform.mapper.domain.User;
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
}
