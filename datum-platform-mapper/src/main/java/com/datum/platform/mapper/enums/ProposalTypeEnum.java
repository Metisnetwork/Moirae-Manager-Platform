package com.datum.platform.mapper.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum ProposalTypeEnum {

    ADD_AUTHORITY(1, "提名加入委员会"),
    KICK_OUT_AUTHORITY(2, "提名退出委员会"),
    AUTO_QUIT_AUTHORITY(3, "主动退出委员会"),
    ;

    @JsonValue
    @EnumValue
    private Integer value;
    private String desc;

    ProposalTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private static Map<Integer, ProposalTypeEnum> map = new HashMap<>();
    static {
        for (ProposalTypeEnum value : ProposalTypeEnum.values()) {
            map.put(value.getValue(),value);
        }
    }
    public static ProposalTypeEnum find(Integer value) {
        return map.get(value);
    }

    public static ProposalTypeEnum find(BigInteger value) {
        return find(value.intValue());
    }
}
