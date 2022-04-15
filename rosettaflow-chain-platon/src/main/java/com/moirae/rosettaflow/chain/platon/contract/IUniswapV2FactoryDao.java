package com.moirae.rosettaflow.chain.platon.contract;

public interface IUniswapV2FactoryDao {

    String WETH();

    void initContractAddress();

    String getPair(String tokenAddress);
}
