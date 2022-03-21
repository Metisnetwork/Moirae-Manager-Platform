package com.moirae.rosettaflow.vo.home;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moirae.rosettaflow.common.constants.SysConstant;
import com.moirae.rosettaflow.common.utils.LanguageContext;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

@Data
@ApiModel(value = "最新的模型列表")
public class LatestModelVo {

    @ApiModelProperty(value = "模型元数据id")
    private String metaDataId;

    @ApiModelProperty(value = "模型的创建过去时间, 单位毫秒")
    public Long getCreateTimeDistance(){
        return new Date().getTime() - createTime.getTime();
    }

    @ApiModelProperty(value = "模型的创建描述")
    public String getModelCreateDesc(){
        if (Objects.nonNull(LanguageContext.get()) && LanguageContext.get().equals(SysConstant.EN_US)) {
            return algorithmNameEn + " Success";
        }
        return algorithmName + "成功";
    }

    @ApiModelProperty(value = "组织的身份标识id")
    private String identityId;

    @ApiModelProperty(value = "组织的身份名称")
    private String nodeName;

    @ApiModelProperty(value = "组织机构图像url")
    private String imageUrl;

    @JsonIgnore
    private String algorithmName;
    @JsonIgnore
    private String algorithmNameEn;
    @JsonIgnore
    private Date createTime;
}
