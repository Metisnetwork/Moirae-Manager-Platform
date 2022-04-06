package com.moirae.rosettaflow.chain.platon.config;

import com.moirae.rosettaflow.chain.platon.enums.Web3jProtocolEnum;
import com.platon.parameters.NetworkParameters;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@ConfigurationProperties(prefix="chain.platon")
@Data
public class PlatONProperties {
    private long chainId;
    private String hrp;
    private String metisPayAddress;
    private String uniswapV2Router02;
    private Web3jProtocolEnum web3jProtocol;
    private List<String> web3jAddresses;

    @PostConstruct
    public void init(){
        NetworkParameters.init(chainId,hrp);
    }
}
