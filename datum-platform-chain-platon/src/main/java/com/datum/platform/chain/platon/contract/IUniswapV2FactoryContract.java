package com.datum.platform.chain.platon.contract;

public interface IUniswapV2FactoryContract {

    String WETH();

    void initContractAddress();

    String getPair(String tokenAddress);
}
