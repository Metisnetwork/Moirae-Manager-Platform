package com.datum.platform.common.validation;


import com.datum.platform.common.annotation.CheckAddress;
import com.datum.platform.common.utils.AddressChangeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author hudenian
 * @date 2021/12/13
 */
@Slf4j
public class AddressValidation implements ConstraintValidator<CheckAddress, String> {
    @Override
    public void initialize(CheckAddress constraintAnnotation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isEmpty(value)) {
            return false;
        } else {
            try {
                return AddressChangeUtils.convert0xAddress(value).toLowerCase().startsWith("0x");
            } catch (Exception e) {
                log.error("钱包地址格式错误, value:{}, 错误信息:{}", value, e.getMessage());
                return false;
            }
        }
    }
}
