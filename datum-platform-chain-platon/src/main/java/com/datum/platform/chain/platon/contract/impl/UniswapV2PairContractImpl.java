package com.datum.platform.chain.platon.contract.impl;

import com.datum.platform.chain.platon.PlatONClient;
import com.datum.platform.chain.platon.contract.IUniswapV2PairContract;
import com.datum.platform.chain.platon.contract.evm.IUniswapV2Pair;
import com.datum.platform.chain.platon.enums.CodeEnum;
import com.datum.platform.chain.platon.exception.AppException;
import com.datum.platform.chain.platon.function.ExceptionFunction;
import com.datum.platform.chain.platon.utils.AddressUtils;
import com.platon.protocol.core.RemoteCall;
import com.platon.tuples.generated.Tuple3;
import com.platon.tx.ReadonlyTransactionManager;
import com.platon.tx.gas.ContractGasProvider;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigInteger;
import java.net.SocketTimeoutException;

@Component
public class UniswapV2PairContractImpl implements IUniswapV2PairContract {

    @Resource
    private PlatONClient platOnClient;

    @Override
    public Tuple3<BigInteger, BigInteger, BigInteger> getReserves(String contractAddress) {
        return query(contract -> contract.getReserves() , contractAddress);
    }

    private <R> R query(ExceptionFunction<IUniswapV2Pair, RemoteCall<R>> supplier, String contractAddress) {
        contractAddress = AddressUtils.hexToBech32(contractAddress);
        ReadonlyTransactionManager transactionManager = new ReadonlyTransactionManager(platOnClient.getWeb3j(), contractAddress);
        try {
            IUniswapV2Pair dataTokenTemplate = IUniswapV2Pair.load(contractAddress, platOnClient.getWeb3j(), transactionManager, new ContractGasProvider(BigInteger.ZERO, BigInteger.ZERO));
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
