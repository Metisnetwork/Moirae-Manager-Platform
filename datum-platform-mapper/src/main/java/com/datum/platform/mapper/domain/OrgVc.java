package com.datum.platform.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 组织的VC
 * </p>
 *
 * @author chendai
 * @since 2022-07-08
 */
@Data
@TableName("mo_org_vc")
public class OrgVc implements Serializable {

    /**
     * 身份认证标识的id
     */
    @TableId
    private String identityId;

    /**
     * vc的颁发者
     */
    private String issuer;

    /**
     * vc的颁发时间
     */
    private Date issuanceDate;

    /**
     * vc的过期时间
     */
    private Date expirationDate;

    /**
     * vc的持有者
     */
    private String holder;

    /**
     * vc的公示信息
     */
    private String publicityId;

    /**
     * vc的内容归档
     */
    private String vcContent;

    /**
     * vc的证明归档
     */
    private String vcProof;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(update = "now()")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

}
