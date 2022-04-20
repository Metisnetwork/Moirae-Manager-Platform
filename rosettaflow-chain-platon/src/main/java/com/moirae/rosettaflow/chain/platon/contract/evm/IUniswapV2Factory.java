package com.moirae.rosettaflow.chain.platon.contract.evm;

import com.platon.abi.solidity.EventEncoder;
import com.platon.abi.solidity.TypeReference;
import com.platon.abi.solidity.datatypes.Address;
import com.platon.abi.solidity.datatypes.Event;
import com.platon.abi.solidity.datatypes.Function;
import com.platon.abi.solidity.datatypes.Type;
import com.platon.abi.solidity.datatypes.generated.Uint256;
import com.platon.crypto.Credentials;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.DefaultBlockParameter;
import com.platon.protocol.core.RemoteCall;
import com.platon.protocol.core.methods.request.PlatonFilter;
import com.platon.protocol.core.methods.response.Log;
import com.platon.protocol.core.methods.response.TransactionReceipt;
import com.platon.tx.Contract;
import com.platon.tx.TransactionManager;
import com.platon.tx.gas.GasProvider;
import rx.Observable;
import rx.functions.Func1;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://github.com/PlatONnetwork/client-sdk-java/releases">platon-web3j command line tools</a>,
 * or the com.platon.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/PlatONnetwork/client-sdk-java/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.1.0.0.
 */
public class IUniswapV2Factory extends Contract {
    private static final String BINARY = "";

    public static final String FUNC_ALLPAIRS = "allPairs";

    public static final String FUNC_ALLPAIRSLENGTH = "allPairsLength";

    public static final String FUNC_CREATEPAIR = "createPair";

    public static final String FUNC_FEETO = "feeTo";

    public static final String FUNC_FEETOSETTER = "feeToSetter";

    public static final String FUNC_GETPAIR = "getPair";

    public static final String FUNC_SETFEETO = "setFeeTo";

    public static final String FUNC_SETFEETOSETTER = "setFeeToSetter";

    public static final Event PAIRCREATED_EVENT = new Event("PairCreated",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    protected IUniswapV2Factory(String contractAddress, Web3j web3j, Credentials credentials, GasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    protected IUniswapV2Factory(String contractAddress, Web3j web3j, TransactionManager transactionManager, GasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<PairCreatedEventResponse> getPairCreatedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(PAIRCREATED_EVENT, transactionReceipt);
        ArrayList<PairCreatedEventResponse> responses = new ArrayList<PairCreatedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            PairCreatedEventResponse typedResponse = new PairCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.token0 = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.token1 = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.pair = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.param3 = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<PairCreatedEventResponse> pairCreatedEventObservable(PlatonFilter filter) {
        return web3j.platonLogObservable(filter).map(new Func1<Log, PairCreatedEventResponse>() {
            @Override
            public PairCreatedEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(PAIRCREATED_EVENT, log);
                PairCreatedEventResponse typedResponse = new PairCreatedEventResponse();
                typedResponse.log = log;
                typedResponse.token0 = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.token1 = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.pair = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.param3 = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<PairCreatedEventResponse> pairCreatedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        PlatonFilter filter = new PlatonFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PAIRCREATED_EVENT));
        return pairCreatedEventObservable(filter);
    }

    public RemoteCall<String> allPairs(BigInteger param0) {
        final Function function = new Function(FUNC_ALLPAIRS,
                Arrays.<Type>asList(new Uint256(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> allPairsLength() {
        final Function function = new Function(FUNC_ALLPAIRSLENGTH,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> createPair(String tokenA, String tokenB) {
        final Function function = new Function(
                FUNC_CREATEPAIR,
                Arrays.<Type>asList(new Address(tokenA),
                new Address(tokenB)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> feeTo() {
        final Function function = new Function(FUNC_FEETO,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> feeToSetter() {
        final Function function = new Function(FUNC_FEETOSETTER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> getPair(String tokenA, String tokenB) {
        final Function function = new Function(FUNC_GETPAIR,
                Arrays.<Type>asList(new Address(tokenA),
                new Address(tokenB)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> setFeeTo(String param0) {
        final Function function = new Function(
                FUNC_SETFEETO,
                Arrays.<Type>asList(new Address(param0)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setFeeToSetter(String param0) {
        final Function function = new Function(
                FUNC_SETFEETOSETTER,
                Arrays.<Type>asList(new Address(param0)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<IUniswapV2Factory> deploy(Web3j web3j, Credentials credentials, GasProvider contractGasProvider) {
        return deployRemoteCall(IUniswapV2Factory.class, web3j, credentials, contractGasProvider, BINARY,  "");
    }

    public static RemoteCall<IUniswapV2Factory> deploy(Web3j web3j, TransactionManager transactionManager, GasProvider contractGasProvider) {
        return deployRemoteCall(IUniswapV2Factory.class, web3j, transactionManager, contractGasProvider, BINARY,  "");
    }

    public static IUniswapV2Factory load(String contractAddress, Web3j web3j, Credentials credentials, GasProvider contractGasProvider) {
        return new IUniswapV2Factory(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static IUniswapV2Factory load(String contractAddress, Web3j web3j, TransactionManager transactionManager, GasProvider contractGasProvider) {
        return new IUniswapV2Factory(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static class PairCreatedEventResponse {
        public Log log;

        public String token0;

        public String token1;

        public String pair;

        public BigInteger param3;
    }
}
