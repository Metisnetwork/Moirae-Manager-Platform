package com.datum.platform.chain.platon.contract;

import com.platon.tuples.generated.Tuple3;

import java.math.BigInteger;

public interface ERC721TemplateContract {
    String name(String contractAddress);
    String symbol(String contractAddress);
    BigInteger totalSupply(String contractAddress);
    Tuple3<String, String, Boolean> getExtInfo(String contractAddress, BigInteger tokenId);
    BigInteger tokenByIndex(String contractAddress, BigInteger index);
    String tokenURI(String contractAddress, BigInteger tokenId);
    String ownerOf(String contractAddress, BigInteger tokenId);
}
