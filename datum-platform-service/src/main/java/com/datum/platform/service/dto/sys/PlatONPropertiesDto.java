package com.datum.platform.service.dto.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "PlatON属性配置")
public class PlatONPropertiesDto {
    @ApiModelProperty(value = "链id")
    private long chainId;
    @ApiModelProperty(value = "链名称")
    private String chainName;
    @ApiModelProperty(value = "json rpc地址")
    private String rpcUrl;
    @ApiModelProperty(value = "币种代码")
    private String symbol;
    @ApiModelProperty(value = "浏览器地址")
    private String blockExplorerUrl;
    @ApiModelProperty(value = "支付助手合约地址")
    private String datumNetworkPayAddress;
    @ApiModelProperty(value = "DEX路由合约地址")
    private String uniswapV2Router02;
    @ApiModelProperty(value = "dex地址")
    private String dexUrl;
    @ApiModelProperty(value = "tofunft地址")
    private String tofunftUrl;

}
