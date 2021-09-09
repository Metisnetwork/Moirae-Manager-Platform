package com.platon.rosettaflow.req.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
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
    @NotNull(message = "{metadata.id.notNull}")
    @Positive(message = "{metadata.id.positive}")
    private Long id;

    @ApiModelProperty(value = "授权方式: 1-按时间, 2-按次数", required = true, example = "1")
    @NotNull(message = "{metadata.auth.notNull}")
    @Range(min = 1, max = 2, message = "{metadata.auth.type.error}")
    private Byte authType;

    @ApiModelProperty(value = "用户类型 0: 未定义; 1: 以太坊地址; 2: Alaya地址; 3: PlatON地址", required = true)
    @NotNull(message = "{user.type.noBlank}")
    @Range(min = 0, max = 3, message = "{user.type.range.error}")
    private Byte userType;

    @ApiModelProperty(value = "授权次数，按次数时,此字段必输,当authType按时间方式时，authValue默认输入0", required = true, example = "100")
    @PositiveOrZero(message = "{metadata.auth.positive}")
    private Integer authValue;

    @ApiModelProperty(value = "授权开始时间,按时间授权此字段必输", example = "2021-08-27 17:13:47")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date authBeginTime;

    @ApiModelProperty(value = "授权结束时间,按时间授权此字段必输", example = "2021-10-27 17:13:47")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date authEndTime;

    @ApiModelProperty(value = "发起数据授权申请的账户的签名", required = true)
    @NotNull(message = "{metadata.auth.sign.notNull}")
    private String sign;
}
