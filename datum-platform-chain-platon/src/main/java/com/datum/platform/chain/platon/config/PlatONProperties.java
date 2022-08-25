package com.datum.platform.chain.platon.config;

import com.datum.platform.chain.platon.enums.Web3jProtocolEnum;
import com.platon.bech32.Bech32;
import com.platon.parameters.NetworkParameters;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.List;

@Component
@ConfigurationProperties(prefix="chain.platon")
@Data
public class PlatONProperties {
    private long chainId;
    private String hrp;
    private String datumNetworkPayAddress;
    private String uniswapV2Router02;
    private String voteAddress;
    private String didAddress;
    private String pctAddress;
    private String credentialAddress;

    private BigInteger voteDeployBlockNumber;
    private Web3jProtocolEnum web3jProtocol;
    private List<String> web3jAddresses;

    private String chainName;
    private String rpcUrl;
    private String blockExplorerUrl;
    private String dexUrl;
    private String symbol;
    private String tofunftUrl;

    public void setDatumNetworkPayAddress(String datumNetworkPayAddress){
        this.datumNetworkPayAddress = datumNetworkPayAddress.toLowerCase();
    }
    public void setUniswapV2Router02(String uniswapV2Router02){
        this.uniswapV2Router02 = uniswapV2Router02.toLowerCase();
    }
    public void setVoteAddress(String voteAddress){
        this.voteAddress = voteAddress.toLowerCase();
    }
    public void setDidAddress(String didAddress){
        this.didAddress = didAddress.toLowerCase();
    }
    public void setPctAddress(String pctAddress){
        this.pctAddress = pctAddress.toLowerCase();
    }
    public void setCredentialAddress(String credentialAddress){
        this.credentialAddress = credentialAddress.toLowerCase();
    }
    public String getVoteLatAddress(){
        return Bech32.addressEncode(hrp, voteAddress);
    }
    public String getDidLatAddress(){
        return Bech32.addressEncode(hrp, didAddress);
    }
    public String getPctLatAddress(){
        return Bech32.addressEncode(hrp, pctAddress);
    }
    public String getCredentialLatAddress(){
        return Bech32.addressEncode(hrp, credentialAddress);
    }

    @PostConstruct
    public void init(){
        NetworkParameters.init(chainId,hrp);
    }
}
