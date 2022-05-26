package com.datum.platform.common.annotation;

import com.datum.platform.common.validation.AddressValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author hudenian
 * @date 2021/12/13
 */

@Documented
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = {AddressValidation.class})
public @interface CheckAddress {
    String message() default "User wallet address format is wrong";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
