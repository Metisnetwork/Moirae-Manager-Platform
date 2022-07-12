package com.datum.platform.mapper.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum ProposalStatusEnum {

    HAS_NOT_STARTED(0, "未开始"),
    VOTING(1, "投票中"),
    VOTE_PASS(2, "投票通过"),
    VOTE_NOT_PASS(3, "投票未通过"),
    EXITING(4, "退出中"),
    SIGNED_OUT(5, "已退出"),
    REVOKED(6, "已撤销"),
    ;

    @JsonValue
    @EnumValue
    private Integer value;
    private String desc;

    ProposalStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private static Map<Integer, ProposalStatusEnum> map = new HashMap<>();
    static {
        for (ProposalStatusEnum value : ProposalStatusEnum.values()) {
            map.put(value.getValue(),value);
        }
    }
    public static ProposalStatusEnum find(Integer value) {
        return map.get(value);
    }

    public static ProposalStatusEnum find(BigInteger value) {
        return find(value.intValue());
    }
}
