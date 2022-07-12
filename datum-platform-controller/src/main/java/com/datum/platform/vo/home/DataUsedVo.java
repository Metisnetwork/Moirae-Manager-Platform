package com.datum.platform.vo.home;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "元数据使用量")
public class DataUsedVo {

    @ApiModelProperty(value = "元数据id")
    private String metaDataId;

    @ApiModelProperty(value = "元数据名称")
    private String metaDataName;

    @ApiModelProperty(value = "元数据使用次数")
    private Long usageCount;
}
