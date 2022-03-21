package com.moirae.rosettaflow.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DataOrderByEnum {

    TOKEN_NAME("tokenName", "凭证名称"),
    TOKEN_HOLD("tokenHold", "凭证持有量"),
    TOKEN_PRICE_DESC("tokenPriceDesc", "凭证价格倒序"),
    TOKEN_PRICE_ASC("tokenPriceAsc", "凭证价格正序"),
    ;

    @JsonValue
    private String value;
    private String desc;

    DataOrderByEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }
}
