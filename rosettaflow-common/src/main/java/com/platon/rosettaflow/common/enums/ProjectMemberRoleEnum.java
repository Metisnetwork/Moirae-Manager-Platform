package com.platon.rosettaflow.common.enums;

/**
 * 项目成员角色
 *
 * @author admin
 * @date 2021/8/16
 */
public enum ProjectMemberRoleEnum {

    /**
     * 管理员
     */
    ADMIN((byte) 1, "管理员"),
    /**
     * 编辑着
     */
    EDIT((byte) 2, "编辑着"),
    /**
     * 查看着
     */
    VIEW((byte) 3, "查看着");

    private final byte roleId;
    private final String desc;

    ProjectMemberRoleEnum(byte roleId, String desc) {
        this.roleId = roleId;
        this.desc = desc;
    }

    public static ProjectMemberRoleEnum getRoleById(byte roleId) {
        for (ProjectMemberRoleEnum e : values()) {
            if (e.getRoleId() == roleId) {
                return e;
            }
        }
        return null;
    }

    public byte getRoleId() {
        return roleId;
    }

    public String getDesc() {
        return desc;
    }
}
