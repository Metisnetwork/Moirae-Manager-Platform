package com.datum.platform.chain.platon.contract.evm;

import com.platon.abi.solidity.TypeReference;
import com.platon.abi.solidity.datatypes.Address;
import com.platon.abi.solidity.datatypes.DynamicArray;
import com.platon.abi.solidity.datatypes.Function;
import com.platon.abi.solidity.datatypes.Type;
import com.platon.abi.solidity.datatypes.generated.Uint256;
import com.platon.crypto.Credentials;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.RemoteCall;
import com.platon.protocol.core.methods.response.TransactionReceipt;
import com.platon.tx.Contract;
import com.platon.tx.TransactionManager;
import com.platon.tx.gas.GasProvider;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://github.com/PlatONnetwork/client-sdk-java/releases">platon-web3j command line tools</a>,
 * or the com.platon.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/PlatONnetwork/client-sdk-java/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.1.0.0.
 */
public class IUniswapV2Router02 extends Contract {
    private static final String BINARY = "";

    public static final String FUNC_WETH = "WETH";

    public static final String FUNC_ADDLIQUIDITY = "addLiquidity";

    public static final String FUNC_ADDLIQUIDITYETH = "addLiquidityETH";

    public static final String FUNC_FACTORY = "factory";

    public static final String FUNC_GETAMOUNTIN = "getAmountIn";

    public static final String FUNC_GETAMOUNTOUT = "getAmountOut";

    public static final String FUNC_GETAMOUNTSIN = "getAmountsIn";

    public static final String FUNC_GETAMOUNTSOUT = "getAmountsOut";

    public static final String FUNC_QUOTE = "quote";

    public static final String FUNC_REMOVELIQUIDITY = "removeLiquidity";

    public static final String FUNC_REMOVELIQUIDITYETH = "removeLiquidityETH";

    public static final String FUNC_REMOVELIQUIDITYETHSUPPORTINGFEEONTRANSFERTOKENS = "removeLiquidityETHSupportingFeeOnTransferTokens";

    public static final String FUNC_REMOVELIQUIDITYETHWITHPERMIT = "removeLiquidityETHWithPermit";

    public static final String FUNC_REMOVELIQUIDITYETHWITHPERMITSUPPORTINGFEEONTRANSFERTOKENS = "removeLiquidityETHWithPermitSupportingFeeOnTransferTokens";

    public static final String FUNC_REMOVELIQUIDITYWITHPERMIT = "removeLiquidityWithPermit";

    public static final String FUNC_SWAPETHFOREXACTTOKENS = "swapETHForExactTokens";

    public static final String FUNC_SWAPEXACTETHFORTOKENS = "swapExactETHForTokens";

    public static final String FUNC_SWAPEXACTETHFORTOKENSSUPPORTINGFEEONTRANSFERTOKENS = "swapExactETHForTokensSupportingFeeOnTransferTokens";

    public static final String FUNC_SWAPEXACTTOKENSFORETH = "swapExactTokensForETH";

    public static final String FUNC_SWAPEXACTTOKENSFORETHSUPPORTINGFEEONTRANSFERTOKENS = "swapExactTokensForETHSupportingFeeOnTransferTokens";

    public static final String FUNC_SWAPEXACTTOKENSFORTOKENS = "swapExactTokensForTokens";

    public static final String FUNC_SWAPEXACTTOKENSFORTOKENSSUPPORTINGFEEONTRANSFERTOKENS = "swapExactTokensForTokensSupportingFeeOnTransferTokens";

    public static final String FUNC_SWAPTOKENSFOREXACTETH = "swapTokensForExactETH";

    public static final String FUNC_SWAPTOKENSFOREXACTTOKENS = "swapTokensForExactTokens";

    protected IUniswapV2Router02(String contractAddress, Web3j web3j, Credentials credentials, GasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    protected IUniswapV2Router02(String contractAddress, Web3j web3j, TransactionManager transactionManager, GasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<String> WETH() {
        final Function function = new Function(FUNC_WETH,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> addLiquidity(String tokenA, String tokenB, BigInteger amountADesired, BigInteger amountBDesired, BigInteger amountAMin, BigInteger amountBMin, String to, BigInteger deadline) {
        final Function function = new Function(
                FUNC_ADDLIQUIDITY,
                Arrays.<Type>asList(new Address(tokenA),
                new Address(tokenB),
                new Uint256(amountADesired),
                new Uint256(amountBDesired),
                new Uint256(amountAMin),
                new Uint256(amountBMin),
                new Address(to),
                new Uint256(deadline)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> addLiquidityETH(String token, BigInteger amountTokenDesired, BigInteger amountTokenMin, BigInteger amountETHMin, String to, BigInteger deadline, BigInteger vonValue) {
        final Function function = new Function(
                FUNC_ADDLIQUIDITYETH,
                Arrays.<Type>asList(new Address(token),
                new Uint256(amountTokenDesired),
                new Uint256(amountTokenMin),
                new Uint256(amountETHMin),
                new Address(to),
                new Uint256(deadline)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, vonValue);
    }

    public RemoteCall<String> factory() {
        final Function function = new Function(FUNC_FACTORY,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> getAmountIn(BigInteger amountOut, BigInteger reserveIn, BigInteger reserveOut) {
        final Function function = new Function(FUNC_GETAMOUNTIN,
                Arrays.<Type>asList(new Uint256(amountOut),
                new Uint256(reserveIn),
                new Uint256(reserveOut)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> getAmountOut(BigInteger amountIn, BigInteger reserveIn, BigInteger reserveOut) {
        final Function function = new Function(FUNC_GETAMOUNTOUT,
                Arrays.<Type>asList(new Uint256(amountIn),
                new Uint256(reserveIn),
                new Uint256(reserveOut)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<List> getAmountsIn(BigInteger amountOut, List<String> path) {
        final Function function = new Function(FUNC_GETAMOUNTSIN,
                Arrays.<Type>asList(new Uint256(amountOut),
                new DynamicArray<Address>(
                Address.class,
                        com.platon.abi.solidity.Utils.typeMap(path, Address.class))),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}));
        return new RemoteCall<List>(
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteCall<List> getAmountsOut(BigInteger amountIn, List<String> path) {
        final Function function = new Function(FUNC_GETAMOUNTSOUT,
                Arrays.<Type>asList(new Uint256(amountIn),
                new DynamicArray<Address>(
                Address.class,
                        com.platon.abi.solidity.Utils.typeMap(path, Address.class))),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}));
        return new RemoteCall<List>(
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteCall<BigInteger> quote(BigInteger amountA, BigInteger reserveA, BigInteger reserveB) {
        final Function function = new Function(FUNC_QUOTE,
                Arrays.<Type>asList(new Uint256(amountA),
                new Uint256(reserveA),
                new Uint256(reserveB)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> removeLiquidity(String tokenA, String tokenB, BigInteger liquidity, BigInteger amountAMin, BigInteger amountBMin, String to, BigInteger deadline) {
        final Function function = new Function(
                FUNC_REMOVELIQUIDITY,
                Arrays.<Type>asList(new Address(tokenA),
                new Address(tokenB),
                new Uint256(liquidity),
                new Uint256(amountAMin),
                new Uint256(amountBMin),
                new Address(to),
                new Uint256(deadline)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> removeLiquidityETH(String token, BigInteger liquidity, BigInteger amountTokenMin, BigInteger amountETHMin, String to, BigInteger deadline) {
        final Function function = new Function(
                FUNC_REMOVELIQUIDITYETH,
                Arrays.<Type>asList(new Address(token),
                new Uint256(liquidity),
                new Uint256(amountTokenMin),
                new Uint256(amountETHMin),
                new Address(to),
                new Uint256(deadline)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> removeLiquidityETHSupportingFeeOnTransferTokens(String token, BigInteger liquidity, BigInteger amountTokenMin, BigInteger amountETHMin, String to, BigInteger deadline) {
        final Function function = new Function(
                FUNC_REMOVELIQUIDITYETHSUPPORTINGFEEONTRANSFERTOKENS,
                Arrays.<Type>asList(new Address(token),
                new Uint256(liquidity),
                new Uint256(amountTokenMin),
                new Uint256(amountETHMin),
                new Address(to),
                new Uint256(deadline)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> removeLiquidityETHWithPermit(String token, BigInteger liquidity, BigInteger amountTokenMin, BigInteger amountETHMin, String to, BigInteger deadline, Boolean approveMax, BigInteger v, byte[] r, byte[] s) {
        final Function function = new Function(
                FUNC_REMOVELIQUIDITYETHWITHPERMIT,
                Arrays.<Type>asList(new Address(token),
                new Uint256(liquidity),
                new Uint256(amountTokenMin),
                new Uint256(amountETHMin),
                new Address(to),
                new Uint256(deadline),
                new com.platon.abi.solidity.datatypes.Bool(approveMax),
                new com.platon.abi.solidity.datatypes.generated.Uint8(v),
                new com.platon.abi.solidity.datatypes.generated.Bytes32(r),
                new com.platon.abi.solidity.datatypes.generated.Bytes32(s)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> removeLiquidityETHWithPermitSupportingFeeOnTransferTokens(String token, BigInteger liquidity, BigInteger amountTokenMin, BigInteger amountETHMin, String to, BigInteger deadline, Boolean approveMax, BigInteger v, byte[] r, byte[] s) {
        final Function function = new Function(
                FUNC_REMOVELIQUIDITYETHWITHPERMITSUPPORTINGFEEONTRANSFERTOKENS,
                Arrays.<Type>asList(new Address(token),
                new Uint256(liquidity),
                new Uint256(amountTokenMin),
                new Uint256(amountETHMin),
                new Address(to),
                new Uint256(deadline),
                new com.platon.abi.solidity.datatypes.Bool(approveMax),
                new com.platon.abi.solidity.datatypes.generated.Uint8(v),
                new com.platon.abi.solidity.datatypes.generated.Bytes32(r),
                new com.platon.abi.solidity.datatypes.generated.Bytes32(s)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> removeLiquidityWithPermit(String tokenA, String tokenB, BigInteger liquidity, BigInteger amountAMin, BigInteger amountBMin, String to, BigInteger deadline, Boolean approveMax, BigInteger v, byte[] r, byte[] s) {
        final Function function = new Function(
                FUNC_REMOVELIQUIDITYWITHPERMIT,
                Arrays.<Type>asList(new Address(tokenA),
                new Address(tokenB),
                new Uint256(liquidity),
                new Uint256(amountAMin),
                new Uint256(amountBMin),
                new Address(to),
                new Uint256(deadline),
                new com.platon.abi.solidity.datatypes.Bool(approveMax),
                new com.platon.abi.solidity.datatypes.generated.Uint8(v),
                new com.platon.abi.solidity.datatypes.generated.Bytes32(r),
                new com.platon.abi.solidity.datatypes.generated.Bytes32(s)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> swapETHForExactTokens(BigInteger amountOut, List<String> path, String to, BigInteger deadline, BigInteger vonValue) {
        final Function function = new Function(
                FUNC_SWAPETHFOREXACTTOKENS,
                Arrays.<Type>asList(new Uint256(amountOut),
                new DynamicArray<Address>(
                Address.class,
                        com.platon.abi.solidity.Utils.typeMap(path, Address.class)),
                new Address(to),
                new Uint256(deadline)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, vonValue);
    }

    public RemoteCall<TransactionReceipt> swapExactETHForTokens(BigInteger amountOutMin, List<String> path, String to, BigInteger deadline, BigInteger vonValue) {
        final Function function = new Function(
                FUNC_SWAPEXACTETHFORTOKENS,
                Arrays.<Type>asList(new Uint256(amountOutMin),
                new DynamicArray<Address>(
                Address.class,
                        com.platon.abi.solidity.Utils.typeMap(path, Address.class)),
                new Address(to),
                new Uint256(deadline)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, vonValue);
    }

    public RemoteCall<TransactionReceipt> swapExactETHForTokensSupportingFeeOnTransferTokens(BigInteger amountOutMin, List<String> path, String to, BigInteger deadline, BigInteger vonValue) {
        final Function function = new Function(
                FUNC_SWAPEXACTETHFORTOKENSSUPPORTINGFEEONTRANSFERTOKENS,
                Arrays.<Type>asList(new Uint256(amountOutMin),
                new DynamicArray<Address>(
                Address.class,
                        com.platon.abi.solidity.Utils.typeMap(path, Address.class)),
                new Address(to),
                new Uint256(deadline)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, vonValue);
    }

    public RemoteCall<TransactionReceipt> swapExactTokensForETH(BigInteger amountIn, BigInteger amountOutMin, List<String> path, String to, BigInteger deadline) {
        final Function function = new Function(
                FUNC_SWAPEXACTTOKENSFORETH,
                Arrays.<Type>asList(new Uint256(amountIn),
                new Uint256(amountOutMin),
                new DynamicArray<Address>(
                Address.class,
                        com.platon.abi.solidity.Utils.typeMap(path, Address.class)),
                new Address(to),
                new Uint256(deadline)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> swapExactTokensForETHSupportingFeeOnTransferTokens(BigInteger amountIn, BigInteger amountOutMin, List<String> path, String to, BigInteger deadline) {
        final Function function = new Function(
                FUNC_SWAPEXACTTOKENSFORETHSUPPORTINGFEEONTRANSFERTOKENS,
                Arrays.<Type>asList(new Uint256(amountIn),
                new Uint256(amountOutMin),
                new DynamicArray<Address>(
                Address.class,
                        com.platon.abi.solidity.Utils.typeMap(path, Address.class)),
                new Address(to),
                new Uint256(deadline)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> swapExactTokensForTokens(BigInteger amountIn, BigInteger amountOutMin, List<String> path, String to, BigInteger deadline) {
        final Function function = new Function(
                FUNC_SWAPEXACTTOKENSFORTOKENS,
                Arrays.<Type>asList(new Uint256(amountIn),
                new Uint256(amountOutMin),
                new DynamicArray<Address>(
                Address.class,
                        com.platon.abi.solidity.Utils.typeMap(path, Address.class)),
                new Address(to),
                new Uint256(deadline)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> swapExactTokensForTokensSupportingFeeOnTransferTokens(BigInteger amountIn, BigInteger amountOutMin, List<String> path, String to, BigInteger deadline) {
        final Function function = new Function(
                FUNC_SWAPEXACTTOKENSFORTOKENSSUPPORTINGFEEONTRANSFERTOKENS,
                Arrays.<Type>asList(new Uint256(amountIn),
                new Uint256(amountOutMin),
                new DynamicArray<Address>(
                Address.class,
                        com.platon.abi.solidity.Utils.typeMap(path, Address.class)),
                new Address(to),
                new Uint256(deadline)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> swapTokensForExactETH(BigInteger amountOut, BigInteger amountInMax, List<String> path, String to, BigInteger deadline) {
        final Function function = new Function(
                FUNC_SWAPTOKENSFOREXACTETH,
                Arrays.<Type>asList(new Uint256(amountOut),
                new Uint256(amountInMax),
                new DynamicArray<Address>(
                Address.class,
                        com.platon.abi.solidity.Utils.typeMap(path, Address.class)),
                new Address(to),
                new Uint256(deadline)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> swapTokensForExactTokens(BigInteger amountOut, BigInteger amountInMax, List<String> path, String to, BigInteger deadline) {
        final Function function = new Function(
                FUNC_SWAPTOKENSFOREXACTTOKENS,
                Arrays.<Type>asList(new Uint256(amountOut),
                new Uint256(amountInMax),
                new DynamicArray<Address>(
                Address.class,
                        com.platon.abi.solidity.Utils.typeMap(path, Address.class)),
                new Address(to),
                new Uint256(deadline)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<IUniswapV2Router02> deploy(Web3j web3j, Credentials credentials, GasProvider contractGasProvider) {
        return deployRemoteCall(IUniswapV2Router02.class, web3j, credentials, contractGasProvider, BINARY,  "");
    }

    public static RemoteCall<IUniswapV2Router02> deploy(Web3j web3j, TransactionManager transactionManager, GasProvider contractGasProvider) {
        return deployRemoteCall(IUniswapV2Router02.class, web3j, transactionManager, contractGasProvider, BINARY,  "");
    }

    public static IUniswapV2Router02 load(String contractAddress, Web3j web3j, Credentials credentials, GasProvider contractGasProvider) {
        return new IUniswapV2Router02(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static IUniswapV2Router02 load(String contractAddress, Web3j web3j, TransactionManager transactionManager, GasProvider contractGasProvider) {
        return new IUniswapV2Router02(contractAddress, web3j, transactionManager, contractGasProvider);
    }
}
