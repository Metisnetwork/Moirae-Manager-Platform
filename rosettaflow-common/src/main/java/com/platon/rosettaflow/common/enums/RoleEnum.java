package com.platon.rosettaflow.common.enums;

/**
 * 返回码定义
 *
 * @author huma
 * @date 2021/5/9
 */
public enum RoleEnum {

    /**
     * 超级管理员
     */
    ADMIN(1L, "超级管理员");

    private final long roleId;
    private final String desc;

    RoleEnum(long roleId, String desc) {
        this.roleId = roleId;
        this.desc = desc;
    }

    public static RoleEnum getRoleById(long roleId) {
        for (RoleEnum e : values()) {
            if (e.getRoleId() == roleId) {
                return e;
            }
        }
        return null;
    }

    public long getRoleId() {
        return roleId;
    }

    public String getDesc() {
        return desc;
    }
}
