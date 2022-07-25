package com.datum.platform.mapper.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum ProposalLogTypeEnum {

    NEWPROPOSAL_EVENT(1, "提交提案"),
    WITHDRAWPROPOSAL_EVENT(2, "撤销提案"),
    VOTEPROPOSAL_EVENT(3, "对提案投票"),
    PROPOSALRESULT_EVENT(4, "投票结果"),
    AUTHORITYADD_EVENT(5, "新增委员会"),
    AUTHORITYDELETE_EVENT(6, "删除委员会");

    @JsonValue
    @EnumValue
    private Integer value;
    private String desc;

    ProposalLogTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private static Map<Integer, ProposalLogTypeEnum> map = new HashMap<>();
    static {
        for (ProposalLogTypeEnum value : ProposalLogTypeEnum.values()) {
            map.put(value.getValue(),value);
        }
    }
    public static ProposalLogTypeEnum find(Integer value) {
        return map.get(value);
    }
}
