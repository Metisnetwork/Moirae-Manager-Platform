package com.platon.rosettaflow.vo.workflow.node;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工作流节点代码响应参数
 * @author hudenian
 * @date 2021/8/30
 */
@Data
@ApiModel(value = "工作流节点代码响应参数")
public class NodeAlgorithmVo {

    @ApiModelProperty(value = "工作流节点代码表主键ID")
    private Long id;

    @ApiModelProperty(value = "工作流节点表主键ID")
    private Long workflowNodeId;

    @ApiModelProperty(value = "算法名称")
    private String algorithmName;

    @ApiModelProperty(value = "算法描述")
    private String algorithmDesc;

    @ApiModelProperty(value = "算法作者")
    private String author;

    @ApiModelProperty(value = "支持协同方最大数量")
    private Long maxNumbers;

    @ApiModelProperty(value = "支持协同方最小数量")
    private Long minNumbers;

    @ApiModelProperty(value = "支持语言,多个以\",\"进行分隔")
    private String supportLanguage;

    @ApiModelProperty(value = "算法编辑类型:1-sql, 2-noteBook")
    private Byte editType;

    @ApiModelProperty(value = "算法代码（计算合约）")
    private String calculateContractCode;

    @ApiModelProperty(value = "数据分片合约")
    private String dataSplitContractCode;

    @ApiModelProperty(value = "所需的内存 (单位: byte)")
    private Long costMem;

    @ApiModelProperty(value = "所需的核数 (单位: 个)")
    private Long costProcessor;

    @ApiModelProperty(value = "GPU核数(单位：核)")
    private Integer costGpu;

    @ApiModelProperty(value = "所需的带宽 (单位: bps)")
    private Long costBandwidth;

}
