package com.datum.platform.chain.platon.contract.impl;

import com.datum.platform.chain.platon.PlatONClient;
import com.datum.platform.chain.platon.contract.ERC721TemplateContract;
import com.datum.platform.chain.platon.contract.evm.ERC721Template;
import com.datum.platform.chain.platon.enums.CodeEnum;
import com.datum.platform.chain.platon.exception.AppException;
import com.datum.platform.chain.platon.function.ExceptionFunction;
import com.datum.platform.chain.platon.utils.AddressUtils;
import com.platon.bech32.Bech32;
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
public class ERC721TemplateContractImpl implements ERC721TemplateContract {

    @Resource
    private PlatONClient platOnClient;

    @Override
    public String name(String contractAddress) {
        return query(contract -> contract.name(), contractAddress);
    }

    @Override
    public String symbol(String contractAddress) {
        return query(contract -> contract.symbol(), contractAddress);
    }

    @Override
    public BigInteger totalSupply(String contractAddress) {
        return query(contract -> contract.totalSupply(), contractAddress);
    }

    @Override
    public Tuple3<String, String, Boolean> getExtInfo(String contractAddress, BigInteger tokenId) {
        Tuple3<String, String, Boolean> result = query(contract -> contract.getExtInfo(tokenId), contractAddress);
        return new Tuple3<>(Bech32.addressDecodeHex(result.getValue1()), result.getValue2(), result.getValue3());
    }

    @Override
    public BigInteger tokenByIndex(String contractAddress, BigInteger index) {
        return query(contract -> contract.tokenByIndex(index), contractAddress);
    }


    private <R> R query(ExceptionFunction<ERC721Template, RemoteCall<R>> supplier, String contractAddress) {
        contractAddress = AddressUtils.hexToBech32(contractAddress);
        ReadonlyTransactionManager transactionManager = new ReadonlyTransactionManager(platOnClient.getWeb3j(), contractAddress);
        try {
            ERC721Template dataTokenTemplate = ERC721Template.load(contractAddress, platOnClient.getWeb3j(), transactionManager, new ContractGasProvider(BigInteger.ZERO, BigInteger.ZERO));
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
