package com.moirae.rosettaflow.chain.platon.contract.impl;

import com.moirae.rosettaflow.chain.platon.PlatONClient;
import com.moirae.rosettaflow.chain.platon.config.PlatONProperties;
import com.moirae.rosettaflow.chain.platon.contract.MetisPayContract;
import com.moirae.rosettaflow.chain.platon.contract.evm.MetisPay;
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
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MetisPayContractImpl implements MetisPayContract {

    @Resource
    private PlatONClient platOnClient;
    @Resource
    private PlatONProperties platONProperties;

    @Override
    public List<String> whitelist(String address) {
        List<String> result = query(contract -> contract.whitelist(AddressUtils.hexToBech32(address)) , platONProperties.getMetisPayAddress());
        return result.stream().map(Bech32::addressDecodeHex).collect(Collectors.toList());
    }


    private <R> R query(ExceptionFunction<MetisPay, RemoteCall<R>> supplier, String contractAddress) {
        contractAddress = AddressUtils.hexToBech32(contractAddress);
        ReadonlyTransactionManager transactionManager = new ReadonlyTransactionManager(platOnClient.getWeb3j(), contractAddress);
        try {
            MetisPay dataTokenTemplate = MetisPay.load(contractAddress, platOnClient.getWeb3j(), transactionManager, new ContractGasProvider(BigInteger.ZERO, BigInteger.ZERO));
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
