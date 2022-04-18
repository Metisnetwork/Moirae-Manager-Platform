package com.moirae.rosettaflow.chain.platon.contract;

import com.platon.tuples.generated.Tuple3;

import java.math.BigInteger;

public interface IUniswapV2PairContract {
   Tuple3<BigInteger, BigInteger, BigInteger> getReserves(String contractAddress);
}
