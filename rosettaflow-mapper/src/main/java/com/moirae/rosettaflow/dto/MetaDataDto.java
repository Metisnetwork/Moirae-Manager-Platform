package com.moirae.rosettaflow.dto;

import com.moirae.rosettaflow.mapper.domain.MetaData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author hudenian
 * @date 2021/8/25
 * @description 功能描述
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MetaDataDto extends MetaData {
    /**
     * 授权状态: -1-未知(1.未登录故获取不到元数据状态 2.用户未申请使用元数据),0-等待审核中, 1-审核通过, 2-审核拒绝
     */
    private Byte authStatus;
    /**
     * 授权方式: 1-按时间, 2-按次数, 3-永久
     */
    private Byte authType;
    /**
     * 数据授权信息有效性状态 (0: 未知; 1: 还未发布的数据授权; 2: 已发布的数据授权; 3: 已撤销的数据授权 <失效前主动撤回的>; 4: 已经失效的数据授权 <过期or达到使用上限的or被拒绝的>;)
     */
    private Byte authMetadataState;
    /**
     * 授权开始时间
     */
    private Date authBeginTime;
    /**
     * 授权结束时间
     */
    private Date authEndTime;
    /**
     * 授权值:按次数单位为（次）
     */
    private Integer authValue;
    /**
     * 授权值:以次数方式申请则显示次数，以时间方式申请则显示时间
     */
    private String authValueStr;




}
