package com.moirae.rosettaflow.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DataOrderByEnum {

    PUBLISHED("publishedAt",  " md.published_at desc","发布时间"),
    TOKEN_NAME("tokenName",  "t.name asc","凭证名称"),
    TOKEN_PRICE_DESC("tokenPriceDesc", "t.price desc", "凭证价格倒序"),
    TOKEN_PRICE_ASC("tokenPriceAsc", "t.price asc",  "凭证价格正序");

    @JsonValue
    private String userValue;
    private String sqlValue;
    private String desc;

    DataOrderByEnum(String userValue, String sqlValue, String desc) {
        this.userValue = userValue;
        this.sqlValue = sqlValue;
        this.desc = desc;
    }

    public String getUserValue() {
        return this.userValue;
    }

    public String getSqlValue(){
        return this.sqlValue;
    }
}
