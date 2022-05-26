package com.datum.platform.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum DataOrderByEnum {
    PUBLISHED("publishedAt",  " dmd.published_at desc","发布时间"),
    TOKEN_NAME("tokenName",  "mt.name","凭证名称"),
    TOKEN_PRICE_DESC("tokenPriceDesc", "mt.price desc", "凭证价格倒序"),
    TOKEN_PRICE_ASC("tokenPriceAsc", "mt.price asc",  "凭证价格正序");

    @JsonValue
    @Getter
    private String userValue;
    @Getter
    private String sqlValue;
    @Getter
    private String desc;

    DataOrderByEnum(String userValue, String sqlValue, String desc) {
        this.userValue = userValue;
        this.sqlValue = sqlValue;
        this.desc = desc;
    }
}
