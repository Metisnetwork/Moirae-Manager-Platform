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
public class MetaDataAuthDetailsVo extends BaseMetaDataVo {

    @ApiModelProperty(value = "组织身份id")
    private String identityId;

    @ApiModelProperty(value = "组织名称")
    private String nodeName;

    @ApiModelProperty(value = "元数据所属行业  1：金融业（银行）、2：金融业（保险）、3：金融业（证券）、4：金融业（其他）、5：ICT、 6：制造业、 7：能源业、 8：交通运输业、 9 ：医疗健康业、 10 ：公共服务业、 11：传媒广告业、 12 ：其他行业'")
    private String industry;

    @ApiModelProperty(value = "源文件的行数")
    private Integer rows;

    @ApiModelProperty(value = "源文件的列数")
    private Integer columns;

    @ApiModelProperty(value = "数据描述")
    private String remarks;

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

    @ApiModelProperty(value = "使用次数(auth_type=2时)")
    private Integer usedTimes;

    @ApiModelProperty(value = "数据描述")
    public String getDataDesc(){
        return remarks;
    }

    @ApiModelProperty(value = "元数据名称")
    public String getDataName(){
        return getMetaDataName();
    }

    @ApiModelProperty(value = "组织名称")
    public String getIdentityName(){
        return nodeName;
    }

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
}
