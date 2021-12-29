package com.moirae.rosettaflow.dto;

import com.moirae.rosettaflow.mapper.domain.UserOrgMaintain;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hudenian
 * @date 2021/12/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserOrgMaintainDto extends UserOrgMaintain {
    /**
     * 组织的身份名称
     */
    private String nodeName;

    /**
     * 是否公共可看的：0-否，1-是
     */
    private Byte publicFlag;

    /**
     * 是否已连接：0-否，1-是
     */
    private Byte connectFlag;

}
