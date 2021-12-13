package com.moirae.rosettaflow.common.validation;


import com.moirae.rosettaflow.common.annotation.CheckAddress;
import com.moirae.rosettaflow.common.utils.AddressChangeUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author hudenian
 * @date 2021/12/13
 */
public class AddressValidation implements ConstraintValidator<CheckAddress, String> {
    @Override
    public void initialize(CheckAddress constraintAnnotation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isEmpty(value)) {
            return false;
        } else {
            return AddressChangeUtils.convert0xAddress(value).toLowerCase().startsWith("0x");
        }
    }
}
