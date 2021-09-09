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
     * 以太坊地址
     */
    ETH(1),
    /**
     * Alaya地址
     */
    ALAYA(2),
    /**
     * PlatON地址
     */
    PLATON(3);

    public static final String HRP_LAT = "lat";
    public static final String HRP_LAX = "lax";
    public static final String HRP_ATP = "atp";
    public static final String HRP_ATX = "atx";
    public static final String HRP_ETH = "0x";
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
            return PLATON.getValue();
        } else if (StrUtil.startWith(lowerAddress, HRP_ATP) || StrUtil.startWith(lowerAddress, HRP_ATX)) {
            return ALAYA.getValue();
        } else if (StrUtil.startWith(lowerAddress, HRP_ETH)) {
            return ETH.getValue();
        } else {
            return UNKNOWN.getValue();
        }
    }

    public int getValue() {
        return value;
    }
}
