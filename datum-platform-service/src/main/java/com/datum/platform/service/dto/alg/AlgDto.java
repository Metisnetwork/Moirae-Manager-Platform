package com.datum.platform.service.dto.alg;

import com.datum.platform.common.utils.LanguageContext;
import com.datum.platform.service.utils.CommonUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "查询算法详情响应参数")
public class AlgDto {

    @ApiModelProperty(value = "算法ID")
    private Long algorithmId;

    @ApiModelProperty(value = "算法名称")
    private String algorithmName;

    @JsonIgnore
    private String algorithmNameEn;

    @ApiModelProperty(value = "算法描述")
    private String algorithmDesc;

    @JsonIgnore
    private String algorithmDescEn;

    @ApiModelProperty(value = "算法图片")
    private String algorithmImageUrl;

    @ApiModelProperty(value = "作者")
    private String author;

    @ApiModelProperty(value = "支持协同方最大数量")
    private Long maxNumbers;

    @ApiModelProperty(value = "支持协同方最小数量")
    private Long minNumbers;

    @ApiModelProperty(value = "支持语言,多个以','进行分隔")
    private String supportLanguage;

    @ApiModelProperty(value = "支持操作系统,多个以','进行分隔")
    private String supportOsSystem;

    @ApiModelProperty(value = "所需的内存 (单位: Mb)")
    private Long costMem;

    @ApiModelProperty(value = "所需的核数 (单位: 个)")
    private Long costCpu;

    @ApiModelProperty(value = "GPU核数 (单位：核)")
    private Integer costGpu;

    @ApiModelProperty(value = "所需的带宽 (单位: Mbps)")
    private Long costBandwidth;

    @ApiModelProperty(value = "所需的运行时长 (单位: 分钟)")
    private Long runTime;

    @ApiModelProperty(value = "是否需要模型: 0-否，1:是")
    private Boolean inputModel;

    @ApiModelProperty(value = "是否支持默认的psi处理: 0-否，1:是")
    private Boolean supportDefaultPsi;

    @ApiModelProperty(value = "输出存储形式: 1-明文，2:密文")
    private Integer storePattern;

    @ApiModelProperty(value = "算法类别：0-密文算法，1-明文算法")
    private Integer type;

    @ApiModelProperty(value = "算法代码")
    private AlgCodeDto algorithmCode;

    @ApiModelProperty(value = "算法代码")
    private List<AlgVariableDto> algorithmVariableList;

    /** 展示时处理内存单位 */
    public Long getCostMem() {
        return CommonUtils.convert2UserOfCostMem(this.costMem);
    }

    /** 展示时处理带宽单位 */
    public Long getCostBandwidth() {
        return CommonUtils.convert2UserOfCostBandwidth(this.costBandwidth);
    }

    /** 展示时最长运行时间单位处理 */
    public Long getRunTime() {
       return CommonUtils.convert2UserOfRunTime(this.runTime);
    }

    public String getAlgorithmName(){
        return LanguageContext.getByLanguage(algorithmName, algorithmNameEn);
    }

    public String getAlgorithmDesc(){
        return LanguageContext.getByLanguage(algorithmDesc, algorithmDescEn);
    }

}
