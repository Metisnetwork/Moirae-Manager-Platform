package com.platon.rosettaflow.common.enums;

import cn.hutool.core.util.StrUtil;

/**
 * @author hudenian
 * @date 2021/8/23
 * @description 用户类型枚举
 */
public enum UserTypeEnum {
    /**
     * 未定义
     */
    UNKNOWN(0),
    /**
     * 第二地址
     */
    SECOND_NETWORK(1),
    /**
     * 测试网地址
     */
    TEST_NETWORK(2),
    /**
     * 主网地址
     */
    MAIN_NETWORK(3);

    public static final String HRP_LAT = "lat";
    public static final String HRP_LAX = "lax";
    public static final String HRP_ATP = "atp";
    public static final String HRP_ATX = "atx";
    public static final String HRP_SECOND = "0x";
    private final int value;
    UserTypeEnum(int value) {
        this.value = value;
    }

    /**
     * check address type
     *
     * @param address address
     * @return address type
     */
    public static int checkUserType(String address) {
        String lowerAddress = address.toLowerCase();
        if (StrUtil.startWith(lowerAddress, HRP_LAT) || StrUtil.startWith(lowerAddress, HRP_LAX)) {
            return MAIN_NETWORK.getValue();
        } else if (StrUtil.startWith(lowerAddress, HRP_ATP) || StrUtil.startWith(lowerAddress, HRP_ATX)) {
            return TEST_NETWORK.getValue();
        } else if (StrUtil.startWith(lowerAddress, HRP_SECOND)) {
            return SECOND_NETWORK.getValue();
        } else {
            return UNKNOWN.getValue();
        }
    }

    public int getValue() {
        return value;
    }
}
