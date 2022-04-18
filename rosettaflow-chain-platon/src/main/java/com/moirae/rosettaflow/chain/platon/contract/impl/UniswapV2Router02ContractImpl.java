package com.moirae.rosettaflow.chain.platon.contract.impl;

import com.moirae.rosettaflow.chain.platon.PlatONClient;
import com.moirae.rosettaflow.chain.platon.config.PlatONProperties;
import com.moirae.rosettaflow.chain.platon.contract.IUniswapV2Router02Contract;
import com.moirae.rosettaflow.chain.platon.contract.evm.IUniswapV2Router02;
import com.moirae.rosettaflow.chain.platon.enums.CodeEnum;
import com.moirae.rosettaflow.chain.platon.exception.AppException;
import com.moirae.rosettaflow.chain.platon.function.ExceptionFunction;
import com.moirae.rosettaflow.chain.platon.utils.AddressUtils;
import com.platon.bech32.Bech32;
import com.platon.protocol.core.RemoteCall;
import com.platon.tx.ReadonlyTransactionManager;
import com.platon.tx.gas.ContractGasProvider;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigInteger;
import java.net.SocketTimeoutException;

@Component
public class UniswapV2Router02ContractImpl implements IUniswapV2Router02Contract {

    @Resource
    private PlatONClient platOnClient;
    @Resource
    private PlatONProperties platONProperties;

    @Override
    public String factory() {
        return Bech32.addressDecodeHex(query(contract -> contract.factory(), platONProperties.getUniswapV2Router02()));
    }

    @Override
    public String WETH() {
        return Bech32.addressDecodeHex(query(contract -> contract.WETH(), platONProperties.getUniswapV2Router02()));
    }

    private <R> R query(ExceptionFunction<IUniswapV2Router02, RemoteCall<R>> supplier, String contractAddress) {
        contractAddress = AddressUtils.hexToBech32(contractAddress);
        ReadonlyTransactionManager transactionManager = new ReadonlyTransactionManager(platOnClient.getWeb3j(), contractAddress);
        try {
            IUniswapV2Router02 dataTokenTemplate = IUniswapV2Router02.load(contractAddress, platOnClient.getWeb3j(), transactionManager, new ContractGasProvider(BigInteger.ZERO, BigInteger.ZERO));
            return supplier.apply(dataTokenTemplate).send();
        }   catch (SocketTimeoutException e) {
            throw new AppException(CodeEnum.CALL_RPC_READ_TIMEOUT,CodeEnum.CALL_RPC_READ_TIMEOUT.getName(),e);
        }  catch (IOException e) {
            throw new AppException(CodeEnum.CALL_RPC_NET_ERROR,CodeEnum.CALL_RPC_NET_ERROR.getName(),e);
        } catch (Exception e) {
            throw new AppException(CodeEnum.CALL_RPC_ERROR,CodeEnum.CALL_RPC_ERROR.getName(),e);
        }
    }
}
