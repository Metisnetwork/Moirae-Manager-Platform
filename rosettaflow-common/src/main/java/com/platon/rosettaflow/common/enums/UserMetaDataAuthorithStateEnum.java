package com.platon.rosettaflow.common.enums;

/**
 * @author hudenian
 * @date 2021/8/23
 * @description 数据授权信息的状态 (0: 未知; 1: 还未发布的数据授权; 2: 已发布的数据授权; 3: 已撤销的数据授权 <失效前主动撤回的>; 4: 已经失效的数据授权 <过期or达到使用上限的or被拒绝的>;)
 */
public enum UserMetaDataAuthorithStateEnum {

    /**
     * 未知
     */
    UNKNOWN((byte) 0),
    /**
     * 还未发布的数据授权
     */
    CREATED((byte) 1),
    /**
     * 已发布的数据授权
     */
    RELEASED((byte) 2),
    /**
     * 已撤销的数据授权 <失效前主动撤回的>
     */
    REVOKED((byte) 3),
    /**
     * 已经失效的数据授权 <过期or达到使用上限的or被拒绝的>
     */
    INVALID((byte) 4);

    private final byte value;

    UserMetaDataAuthorithStateEnum(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

}
