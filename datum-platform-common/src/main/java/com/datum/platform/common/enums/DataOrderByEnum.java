package com.datum.platform.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum DataOrderByEnum {
    PUBLISHED("publishedAt",  " dmd.published_at desc","发布时间");

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
