package com.moirae.rosettaflow.chain.platon.contract;

import java.math.BigInteger;

public interface DataTokenTemplateContract {

    /**
     * 查询代币余额
     *
     * @param contractAddress 代币合约地址
     * @param account 账户信息
     * @return
     */
    BigInteger balanceOf(String contractAddress, String account);

    /**
     * 查询授权给支付助手的代币余额
     *
     * @param contractAddress 代币合约地址
     * @param account 账户信息
     * @return
     */
    BigInteger allowance(String contractAddress, String account);


    /**
     * 查询token名称
     *
     * @param contractAddress 代币合约地址
     * @return
     */
    String name(String contractAddress);


    /**
     * 查询token名称
     *
     * @param contractAddress 代币合约地址
     * @return
     */
    String symbol(String contractAddress);

    /**
     * 查询授权给支付助手的代币余额
     *
     * @param contractAddress 代币合约地址
     * @return
     */
    BigInteger decimals(String contractAddress);
}
