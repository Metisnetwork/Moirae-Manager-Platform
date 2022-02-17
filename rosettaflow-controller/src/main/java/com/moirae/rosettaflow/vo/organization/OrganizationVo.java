package com.moirae.rosettaflow.vo.organization;

import com.moirae.rosettaflow.mapper.enums.OrgStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author hudenian
 * @date 2021/12/15
 */
@Data
@ApiModel(value = "用户维护组织信息详情返回参数")
public class OrganizationVo {

    @ApiModelProperty(value = "组织的身份名称")
    private String nodeName;

    @ApiModelProperty(value = "组织的身份标识Id")
    private String identityId;

    @ApiModelProperty(value = "组织的ip")
    private String identityIp;

    @ApiModelProperty(value = "组织的端口")
    private Integer identityPort;

    @ApiModelProperty(value = "组织的状态 状态,1-Normal; 2-NonNormal")
    private OrgStatusEnum status;

    @ApiModelProperty(value = "组织的最新更新时间")
    private Date updateAt;

    @ApiModelProperty(value = "是否公共可看的：0-否，1-是")
    private Byte publicFlag;

    @ApiModelProperty(value = "组织的身份名称")
    public String getOrgName(){
        return nodeName;
    }

    @ApiModelProperty(value = "总文件数")
    private Integer totalFile;

    @ApiModelProperty(value = "总文件大小(字节)")
    private Long totalData;
}
