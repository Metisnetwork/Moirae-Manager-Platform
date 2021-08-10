package com.platon.rosettaflow.annotation;

import com.platon.rosettaflow.common.enums.RoleEnum;

import java.lang.annotation.*;

/**
 * @author admin
 * @date 2021/7/20
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Roles {
    RoleEnum[] value();
}
