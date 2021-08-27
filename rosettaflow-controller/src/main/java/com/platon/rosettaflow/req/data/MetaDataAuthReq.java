package com.platon.rosettaflow.req.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;

/**
 * @author hudenian
 * @date 2021/8/27
 * @description 元数据授权申请
 */
@Data
@ApiModel(value = "元数据授权申请对象")
public class MetaDataAuthReq {

    @ApiModelProperty(value = "元数据表id", required = true)
    @NotNull(message = "元数据表ID不能为空")
    @Positive(message = "元数据表ID错误")
    private Long id;

    @ApiModelProperty(value = "授权方式: 1-按时间, 2-按次数", required = true, example = "1")
    @NotNull(message = "授权方式不能为空")
    @Range(min = 1, max = 2, message = "授权方式错误")
    private Byte authType;

    @ApiModelProperty(value = "授权次数，按次数时,此字段必输", required = true, example = "100")
    private Long authValue;

    @ApiModelProperty(value = "授权开始时间,按时间授权此字段必输", example = "2021-08-27 17:13:47")
    private Date authBeginTime;

    @ApiModelProperty(value = "授权结束时间,按时间授权此字段必输", example = "2021-10-27 17:13:47")
    private Date authEndTime;

    @ApiModelProperty(value = "发起数据授权申请的账户的签名", required = true)
    @NotNull(message = "发起数据授权申请的账户的签名不能为空")
    private String sign;
}
