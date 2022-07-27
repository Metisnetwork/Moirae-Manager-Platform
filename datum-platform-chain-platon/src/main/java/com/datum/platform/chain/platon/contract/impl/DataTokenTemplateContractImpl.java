package com.datum.platform.chain.platon.contract.impl;

import com.datum.platform.chain.platon.PlatONClient;
import com.datum.platform.chain.platon.config.PlatONProperties;
import com.datum.platform.chain.platon.contract.DataTokenTemplateContract;
import com.datum.platform.chain.platon.contract.evm.DataTokenTemplate;
import com.datum.platform.chain.platon.enums.CodeEnum;
import com.datum.platform.chain.platon.exception.AppException;
import com.datum.platform.chain.platon.function.ExceptionFunction;
import com.datum.platform.chain.platon.utils.AddressUtils;
import com.platon.protocol.core.RemoteCall;
import com.platon.tx.ReadonlyTransactionManager;
import com.platon.tx.gas.ContractGasProvider;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigInteger;
import java.net.SocketTimeoutException;

@Component
public class DataTokenTemplateContractImpl implements DataTokenTemplateContract {

    @Resource
    private PlatONClient platOnClient;
    @Resource
    private PlatONProperties platONProperties;


    @Override
    public BigInteger balanceOf(String contractAddress, String account) {
        return query(contract -> contract.balanceOf(AddressUtils.hexToBech32(account)), contractAddress);
    }

    @Override
    public BigInteger allowance(String contractAddress, String account) {
        return query(contract -> contract.allowance(AddressUtils.hexToBech32(account), AddressUtils.hexToBech32(platONProperties.getDatumNetworkPayAddress())), contractAddress);
    }

    @Override
    public String name(String contractAddress) {
        return query(contract -> contract.name(), contractAddress);
    }

    @Override
    public String symbol(String contractAddress) {
        return query(contract -> contract.symbol(), contractAddress);
    }

    @Override
    public BigInteger decimals(String contractAddress) {
        return query(contract -> contract.decimals(), contractAddress);
    }

    private <R> R query(ExceptionFunction<DataTokenTemplate, RemoteCall<R>> supplier, String contractAddress) {
        contractAddress = AddressUtils.hexToBech32(contractAddress);
        ReadonlyTransactionManager transactionManager = new ReadonlyTransactionManager(platOnClient.getWeb3j(), contractAddress);
        try {
            DataTokenTemplate dataTokenTemplate = DataTokenTemplate.load(contractAddress, platOnClient.getWeb3j(), transactionManager, new ContractGasProvider(BigInteger.ZERO, BigInteger.ZERO));
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
