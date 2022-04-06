package com.moirae.rosettaflow.chain.platon.contract;

public interface IUniswapV2FactoryDao {

    String getPair(String contractAddress, String wEthAddress, String tokenAddress);

}
