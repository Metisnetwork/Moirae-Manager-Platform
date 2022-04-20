package com.moirae.rosettaflow.chain.platon.contract.evm;

import com.platon.abi.solidity.EventEncoder;
import com.platon.abi.solidity.FunctionEncoder;
import com.platon.abi.solidity.TypeReference;
import com.platon.abi.solidity.datatypes.*;
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
public class DataTokenFactory extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b506040516020806109638339810180604052602081101561003057600080fd5b50516001600160a01b038116610091576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602a815260200180610939602a913960400191505060405180910390fd5b600080546001600160a01b039092166001600160a01b0319909216919091179055610878806100c16000396000f3fe608060405234801561001057600080fd5b50600436106100365760003560e01c80632d2d67121461003b578063490aad7614610217575b600080fd5b6101fb600480360360a081101561005157600080fd5b81019060208101813564010000000081111561006c57600080fd5b82018360208201111561007e57600080fd5b803590602001918460018302840111640100000000831117156100a057600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092959493602081019350359150506401000000008111156100f357600080fd5b82018360208201111561010557600080fd5b8035906020019184600183028401116401000000008311171561012757600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929584359560208601359591945092506060810191506040013564010000000081111561018657600080fd5b82018360208201111561019857600080fd5b803590602001918460018302840111640100000000831117156101ba57600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092955061021f945050505050565b604080516001600160a01b039092168252519081900360200190f35b6101fb6106f7565b60008361026057604051600160e51b62461bcd0281526004018080602001828103825260298152602001806108246029913960400191505060405180910390fd5b600054610275906001600160a01b0316610706565b90506001600160a01b0381166102bf57604051600160e51b62461bcd0281526004018080602001828103825260418152602001806107e36041913960600191505060405180910390fd5b6000819050806001600160a01b0316634d4e8b468888338989896040518763ffffffff1660e01b8152600401808060200180602001876001600160a01b03166001600160a01b031681526020018681526020018581526020018060200184810384528a818151815260200191508051906020019080838360005b83811015610351578181015183820152602001610339565b50505050905090810190601f16801561037e5780820380516001836020036101000a031916815260200191505b5084810383528951815289516020918201918b019080838360005b838110156103b1578181015183820152602001610399565b50505050905090810190601f1680156103de5780820380516001836020036101000a031916815260200191505b50848103825285518152855160209182019187019080838360005b838110156104115781810151838201526020016103f9565b50505050905090810190601f16801561043e5780820380516001836020036101000a031916815260200191505b509950505050505050505050602060405180830381600087803b15801561046457600080fd5b505af1158015610478573d6000803e3d6000fd5b505050506040513d602081101561048e57600080fd5b50516104ce57604051600160e51b62461bcd0281526004018080602001828103825260358152602001806107ae6035913960400191505060405180910390fd5b866040518082805190602001908083835b602083106104fe5780518252601f1990920191602091820191016104df565b5181516020939093036101000a600019018019909116921691909117905260405192018290038220600080549195506001600160a01b039182169450908716927fb51c8cbe199ffe8b0d1d39b62d473569750653cb18b165f77ae423b3900180ad9250a4336001600160a01b0316826001600160a01b03167f6af1b2b80c1bbb5a423dde741a2f51a662ca7ec74e7b32c4b74e20f54a247ed3898989898960405180806020018060200186815260200185815260200180602001848103845289818151815260200191508051906020019080838360005b838110156105ed5781810151838201526020016105d5565b50505050905090810190601f16801561061a5780820380516001836020036101000a031916815260200191505b5084810383528851815288516020918201918a019080838360005b8381101561064d578181015183820152602001610635565b50505050905090810190601f16801561067a5780820380516001836020036101000a031916815260200191505b50848103825285518152855160209182019187019080838360005b838110156106ad578181015183820152602001610695565b50505050905090810190601f1680156106da5780820380516001836020036101000a031916815260200191505b509850505050505050505060405180910390a35095945050505050565b6000546001600160a01b031690565b6000808260601b90506040517f3d602d80600a3d3981f3363d3d373d3d3d363d7300000000000000000000000081528160148201527f5af43d82803e903d91602b57fd5bf3000000000000000000000000000000000060288201526037816000f0604080516001600160a01b038316815290519194507f117c72e6c25f0a072e36e148df71468ce2f3dbe7defec5b2c257a6e3eb65278c925081900360200190a15091905056fe44617461546f6b656e466163746f72793a20556e61626c6520746f20696e697469616c697a6520746f6b656e20696e7374616e636544617461546f6b656e466163746f72793a204661696c656420746f20706572666f726d206d696e696d616c206465706c6f79206f662061206e657720746f6b656e44617461546f6b656e466163746f72793a207a65726f20636170206973206e6f7420616c6c6f776564a165627a7a7230582040b86148a86b81465c12b1df5ca899ae057d667a9ddef9838d2ce8e810bd738c002944617461546f6b656e466163746f72793a20496e76616c69642074656d706c6174652061646472657373";

    public static final String FUNC_CREATETOKEN = "createToken";

    public static final String FUNC_GETTOKENTEMPLATE = "getTokenTemplate";

    public static final Event TOKENCREATED_EVENT = new Event("TokenCreated",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Utf8String>(true) {}));
    ;

    public static final Event TOKENREGISTERED_EVENT = new Event("TokenRegistered",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>(true) {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event INSTANCEDEPLOYED_EVENT = new Event("InstanceDeployed",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    protected DataTokenFactory(String contractAddress, Web3j web3j, Credentials credentials, GasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    protected DataTokenFactory(String contractAddress, Web3j web3j, TransactionManager transactionManager, GasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<TransactionReceipt> createToken(String name, String symbol, BigInteger cap, BigInteger initialSupply, String proof) {
        final Function function = new Function(
                FUNC_CREATETOKEN,
                Arrays.<Type>asList(new Utf8String(name),
                new Utf8String(symbol),
                new Uint256(cap),
                new Uint256(initialSupply),
                new Utf8String(proof)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> getTokenTemplate() {
        final Function function = new Function(FUNC_GETTOKENTEMPLATE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public static RemoteCall<DataTokenFactory> deploy(Web3j web3j, Credentials credentials, GasProvider contractGasProvider, String _template) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(_template)));
        return deployRemoteCall(DataTokenFactory.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<DataTokenFactory> deploy(Web3j web3j, TransactionManager transactionManager, GasProvider contractGasProvider, String _template) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(_template)));
        return deployRemoteCall(DataTokenFactory.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    public List<TokenCreatedEventResponse> getTokenCreatedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(TOKENCREATED_EVENT, transactionReceipt);
        ArrayList<TokenCreatedEventResponse> responses = new ArrayList<TokenCreatedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            TokenCreatedEventResponse typedResponse = new TokenCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.newTokenAddress = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.templateAddress = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.tokenName = (byte[]) eventValues.getIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TokenCreatedEventResponse> tokenCreatedEventObservable(PlatonFilter filter) {
        return web3j.platonLogObservable(filter).map(new Func1<Log, TokenCreatedEventResponse>() {
            @Override
            public TokenCreatedEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(TOKENCREATED_EVENT, log);
                TokenCreatedEventResponse typedResponse = new TokenCreatedEventResponse();
                typedResponse.log = log;
                typedResponse.newTokenAddress = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.templateAddress = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.tokenName = (byte[]) eventValues.getIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<TokenCreatedEventResponse> tokenCreatedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        PlatonFilter filter = new PlatonFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TOKENCREATED_EVENT));
        return tokenCreatedEventObservable(filter);
    }

    public List<TokenRegisteredEventResponse> getTokenRegisteredEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(TOKENREGISTERED_EVENT, transactionReceipt);
        ArrayList<TokenRegisteredEventResponse> responses = new ArrayList<TokenRegisteredEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            TokenRegisteredEventResponse typedResponse = new TokenRegisteredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.tokenAddress = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.registeredBy = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.tokenName = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.tokenSymbol = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.tokenCap = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.initialSupply = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.proof = (String) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TokenRegisteredEventResponse> tokenRegisteredEventObservable(PlatonFilter filter) {
        return web3j.platonLogObservable(filter).map(new Func1<Log, TokenRegisteredEventResponse>() {
            @Override
            public TokenRegisteredEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(TOKENREGISTERED_EVENT, log);
                TokenRegisteredEventResponse typedResponse = new TokenRegisteredEventResponse();
                typedResponse.log = log;
                typedResponse.tokenAddress = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.registeredBy = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.tokenName = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.tokenSymbol = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.tokenCap = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.initialSupply = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse.proof = (String) eventValues.getNonIndexedValues().get(4).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<TokenRegisteredEventResponse> tokenRegisteredEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        PlatonFilter filter = new PlatonFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TOKENREGISTERED_EVENT));
        return tokenRegisteredEventObservable(filter);
    }

    public List<InstanceDeployedEventResponse> getInstanceDeployedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(INSTANCEDEPLOYED_EVENT, transactionReceipt);
        ArrayList<InstanceDeployedEventResponse> responses = new ArrayList<InstanceDeployedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            InstanceDeployedEventResponse typedResponse = new InstanceDeployedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.instance = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<InstanceDeployedEventResponse> instanceDeployedEventObservable(PlatonFilter filter) {
        return web3j.platonLogObservable(filter).map(new Func1<Log, InstanceDeployedEventResponse>() {
            @Override
            public InstanceDeployedEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(INSTANCEDEPLOYED_EVENT, log);
                InstanceDeployedEventResponse typedResponse = new InstanceDeployedEventResponse();
                typedResponse.log = log;
                typedResponse.instance = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<InstanceDeployedEventResponse> instanceDeployedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        PlatonFilter filter = new PlatonFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(INSTANCEDEPLOYED_EVENT));
        return instanceDeployedEventObservable(filter);
    }

    public static DataTokenFactory load(String contractAddress, Web3j web3j, Credentials credentials, GasProvider contractGasProvider) {
        return new DataTokenFactory(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static DataTokenFactory load(String contractAddress, Web3j web3j, TransactionManager transactionManager, GasProvider contractGasProvider) {
        return new DataTokenFactory(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static class TokenCreatedEventResponse {
        public Log log;

        public String newTokenAddress;

        public String templateAddress;

        public byte[] tokenName;
    }

    public static class TokenRegisteredEventResponse {
        public Log log;

        public String tokenAddress;

        public String registeredBy;

        public String tokenName;

        public String tokenSymbol;

        public BigInteger tokenCap;

        public BigInteger initialSupply;

        public String proof;
    }

    public static class InstanceDeployedEventResponse {
        public Log log;

        public String instance;
    }
}
