package com.datum.platform.chain.platon;

import com.datum.platform.chain.platon.config.PlatONProperties;
import com.datum.platform.chain.platon.enums.CodeEnum;
import com.datum.platform.chain.platon.enums.Web3jProtocolEnum;
import com.datum.platform.chain.platon.exception.AppException;
import com.datum.platform.chain.platon.function.ExceptionSupplier;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.*;
import com.platon.protocol.core.methods.response.PlatonBlock;
import com.platon.protocol.core.methods.response.Transaction;
import com.platon.protocol.core.methods.response.TransactionReceipt;
import com.platon.protocol.http.HttpService;
import com.platon.protocol.websocket.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PlatONClient {

    @Resource
    private PlatONProperties specialNodeProperties;

    private List<String> nodeUrlList;

    private List<Web3j> web3List = new ArrayList<>();
    private int web3Index = -1;
    private BigInteger maxBlockNumber = BigInteger.ZERO;

    @PostConstruct
    public void init(){
        init(specialNodeProperties.getWeb3jProtocol(), specialNodeProperties.getWeb3jAddresses(), specialNodeProperties.getChainId());
    }

    private void init(Web3jProtocolEnum web3jProtocolEnum, List<String> web3jAddresses, long chainId) {
        this.nodeUrlList = web3jAddresses.stream().map(item-> web3jProtocolEnum.getHead() + item).collect(Collectors.toList());
        // 初始化所有web3j实例
        nodeUrlList.forEach(address ->
                getWeb3j(address).ifPresent( item -> web3List.add(item))
        );
        // 选择最佳节点
        selectBestNode();
    }


    public synchronized Web3j getWeb3j() {
        return web3List.get(web3Index);
    }

    public void selectBestNode(){
        selectBestNodeInner();
    }

    public BigInteger platonGetBalance(String address) {
        return queryRpc(() -> getWeb3j().platonGetBalance(address, DefaultBlockParameterName.LATEST))
                .getBalance();
    }

    public BigInteger platonGetBalance(String address, Long blockNumer) {
        return queryRpc(() -> getWeb3j().platonGetBalance(address,  new DefaultBlockParameterNumber(blockNumer)))
                .getBalance();
    }

    public BigInteger platonBlockNumber() {
        return queryRpc(() -> getWeb3j().platonBlockNumber())
                .getBlockNumber();
    }

    public PlatonBlock.Block platonGetBlockByNumber(BigInteger bn) {
        return queryRpc(() -> getWeb3j().platonGetBlockByNumber(DefaultBlockParameter.valueOf(bn), false))
                .getBlock();
    }

    public Optional<TransactionReceipt> platonGetTransactionReceipt(String hash) {
        return queryRpc(() -> getWeb3j().platonGetTransactionReceipt(hash))
                .getTransactionReceipt();
    }

    public Optional<Transaction> platonGetTransactionByHash(String hash) {
        return queryRpc(() -> getWeb3j().platonGetTransactionByHash(hash))
                .getTransaction();
    }

    private void selectBestNodeInner() {
        BigInteger bestBn = BigInteger.ZERO;
        int bestIndex = -1;

        for (int i = 0; i < web3List.size(); i++) {
            try {
                BigInteger bn = web3List.get(i).platonBlockNumber().send().getBlockNumber();
                if (bn.compareTo(bestBn) > 0) {
                    bestBn = bn;
                    bestIndex = i;
                }
            } catch (Exception e) {
                log.error("check node exception ! ", e);
                Optional<Web3j> optional = getWeb3j(nodeUrlList.get(i));
                if (optional.isPresent()) {
                    Web3j web3j = optional.get();
                    web3List.set(i, web3j);
                    //如果是ws的话，存在连接失效
                    if (web3Index == i) {
                        updateWeb3j(i);
                    }
                }
            }
        }

        if (bestIndex == -1) {
            throw new AppException(CodeEnum.NO_NODE_AVAILABLE, CodeEnum.NO_NODE_AVAILABLE.getName());
        }

        if (bestIndex != web3Index) {
            log.info("Nodes need switched! usedNode = {} curBn = {}",  nodeUrlList.get(bestIndex), bestBn);
            updateWeb3j(bestIndex);
        } else {
            log.info("Nodes no need switched!usedNode = {} curBn = {}",  nodeUrlList.get(web3Index), bestBn);
        }

        if (maxBlockNumber.compareTo(bestBn) > 0) {
            log.warn("maxBlockNumber > bestBlockNumber is error");
        } else {
            maxBlockNumber = bestBn;
        }
    }

    private synchronized void updateWeb3j(int index) {
        web3Index = index;
    }

    private Optional<Web3j> getWeb3j(String address) {
        if (address.startsWith("http")) {
            return Optional.of(Web3j.build(new HttpService(address,false)));
        }
        if (address.startsWith("ws")) {
            WebSocketService webSocketService = new WebSocketService(address,false);
            try {
                webSocketService.connect();
            } catch (ConnectException e) {
                log.error("ws connect exception ! ", e);
            }
            return Optional.of(Web3j.build(webSocketService));
        }
        return Optional.empty();
    }

    private <R extends Response<?>> R queryRpc(ExceptionSupplier<Request<?, R>> supplier) {
        R response;
        try {
            response = supplier.get().send();
        }   catch (SocketTimeoutException e) {
            throw new AppException(CodeEnum.CALL_RPC_READ_TIMEOUT,CodeEnum.CALL_RPC_READ_TIMEOUT.getName(),e);
        }  catch (IOException e) {
            throw new AppException(CodeEnum.CALL_RPC_NET_ERROR,CodeEnum.CALL_RPC_NET_ERROR.getName(),e);
        } catch (Exception e) {
            throw new AppException(CodeEnum.CALL_RPC_ERROR,CodeEnum.CALL_RPC_ERROR.getName(),e);
        }

        if(response == null){
            throw new AppException(CodeEnum.CALL_RPC_BIZ_ERROR, CodeEnum.CALL_RPC_BIZ_ERROR.getName());
        }

        if(response.hasError()){
            int rpcCode = response.getError().getCode();
            String rpcMessage = response.getError().getMessage();
            if (rpcCode == -32000) {
                if ("nonce too low".equals(rpcMessage)) {
                    throw new AppException(CodeEnum.TX_NONCE_TOO_LOW, rpcMessage);
                } else if ("insufficient funds for gas * price + value".equals(rpcMessage)) {
                    throw new AppException(CodeEnum.TX_INSUFFICIENT_FUND, rpcMessage);
                } else if ("exceeds block gas limit".equals(rpcMessage)) {
                    throw new AppException(CodeEnum.TX_EXCEEDS_BLOCK_GAS_LIMIT, rpcMessage);
                } else {
                    throw new AppException(CodeEnum.TX_KNOWN_TX, rpcMessage);
                }
            }
            throw new AppException(CodeEnum.CALL_RPC_BIZ_ERROR, rpcMessage);
        }
        return response;
    }

}
