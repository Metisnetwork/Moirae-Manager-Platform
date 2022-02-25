package com.moirae.rosettaflow.vo.data;

import com.moirae.rosettaflow.mapper.enums.MetaDataAuthOptionEnum;
import com.moirae.rosettaflow.mapper.enums.MetaDataAuthStatusEnum;
import com.moirae.rosettaflow.mapper.enums.MetaDataAuthTypeEnum;
import com.moirae.rosettaflow.mapper.enums.MetaDataStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "元数据授权信息")
public class MetaDataAuthVo extends BaseMetaDataVo {

    @ApiModelProperty(value = "组织身份id")
    private String identityId;

    @ApiModelProperty(value = "组织名称")
    private String nodeName;

    @ApiModelProperty(value = "授权id")
    private String metaDataAuthId;

    @ApiModelProperty(value = "授权申请时间，精确到毫秒")
    private Date applyAt;

    @ApiModelProperty(value = "申请收集授权类型：(0: 未定义; 1: 按照时间段来使用; 2: 按照次数来使用)")
    private MetaDataAuthTypeEnum authType;

    @ApiModelProperty(value = "授权开始时间(auth_type=1时)")
    private Date startAt;

    @ApiModelProperty(value = "授权结束时间(auth_type=1时)")
    private Date endAt;

    @ApiModelProperty(value = "授权次数(auth_type=2时)")
    private Integer times;

    @ApiModelProperty(value = "审核结果，0：等待审核中；1：审核通过；2：审核拒绝")
    private MetaDataAuthOptionEnum auditOption;

    @ApiModelProperty(value = "审核结果，0：等待审核中；1：审核通过；2：审核拒绝")
    private MetaDataAuthStatusEnum authStatus;

    @ApiModelProperty(value = "授权开始时间(auth_type=1时)")
    public Date getAuthBeginTime(){
        return startAt;
    }

    @ApiModelProperty(value = "授权结束时间(auth_type=1时)")
    public Date getAuthEndTime(){
        return endAt;
    }

    @ApiModelProperty(value = "授权次数(auth_type=2时)")
    public Integer getAuthValue(){
        return times;
    }

    @ApiModelProperty(value = "授权申请时间，精确到毫秒")
    public Date getApplyTime(){
        return applyAt;
    }

    @ApiModelProperty(value = "授权id")
    public String getId(){
        return metaDataAuthId;
    }

    @ApiModelProperty(value = "元数据名称")
    public String getDataName(){
        return getMetaDataName();
    }

    @ApiModelProperty(value = "组织名称")
    public String getIdentityName(){
        return nodeName;
    }

    @ApiModelProperty(value = "授权状态， 0-申请中, 1-已授权, 2-已拒绝, 3-已撤销, 4-已失效")
    public Integer getAuthStatus(){
        //申请中
        if(auditOption == MetaDataAuthOptionEnum.PENDING
                && authStatus == MetaDataAuthStatusEnum.PUBLISHED
                && getStatus() == MetaDataStatusEnum.PUBLISHED){
            return 0;
        }

        //已授权
        if(auditOption == MetaDataAuthOptionEnum.PASSED
                && authStatus == MetaDataAuthStatusEnum.PUBLISHED
                && getStatus() == MetaDataStatusEnum.PUBLISHED){
            return 1;
        }

        //已拒绝
        if(auditOption == MetaDataAuthOptionEnum.REFUSED
                && authStatus == MetaDataAuthStatusEnum.PUBLISHED
                && getStatus() == MetaDataStatusEnum.PUBLISHED){
            return 2;
        }

        // 已撤销
        if(authStatus == MetaDataAuthStatusEnum.REVOKED
                && getStatus() == MetaDataStatusEnum.PUBLISHED){
            return 3;
        }

        // 已失效
        return 4;
    }

    @ApiModelProperty(value = "操作: 0-查看详情, 1-重新申请, 2-撤销申请")
    public Integer getActionShow(){
        // 重新申请
        if(auditOption == MetaDataAuthOptionEnum.REFUSED
                && authStatus == MetaDataAuthStatusEnum.PUBLISHED
                && getStatus() == MetaDataStatusEnum.PUBLISHED){
            return 1;
        }

        // 撤销申请
        if(auditOption == MetaDataAuthOptionEnum.PENDING
                && authStatus == MetaDataAuthStatusEnum.PUBLISHED
                && getStatus() == MetaDataStatusEnum.PUBLISHED){
            return 2;
        }

        // 查看详情
        return 0;
    }


}
