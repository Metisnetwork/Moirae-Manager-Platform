package com.platon.rosettaflow.req.project.temp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;

/**
 * 添加项目模板请求参数
 * @author houz
 */
@Data
@ApiModel(value = "添加项目模板请求参数")
public class AddProjTempReq {

    @ApiModelProperty(value = "项目名称", required = true)
    @NotNull(message = "{project.id.notNull}")
    private Long id;



}
